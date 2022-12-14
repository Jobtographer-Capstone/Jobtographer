// CARD BUILDER
function cardBuilder(length) {
    console.log(length)
    let x = 0;
    if (length > 5) {
        length = 5
    }
    while (x < length) { // was x < 5

        let card = '<div class="job-card-populate shadow-lg job_Card">' +
            '<p class="job_Title font-title" th:name="title"></p>' +
            '<h4 class="font-title">Outlook: </h4>'+
            '<p class="job_Outlook font-body" th:name="outlook"></p>' +
            '<h4 class="font-title">Average Salary: </h4>'+
            '<p class="job_Wages font-body" th:name="wages"></p>' +
            '<h4 class="font-title">Certifications: </h4> '+
            '<p class="job_Certs cert-name font-body" th:name="certs"></p>' + '<div>' +
            '<button class="startRoadMap btn" type="button">Create RoadMap</button>' +
            '</div>';
        document.querySelector(".populate").insertAdjacentHTML("beforeEnd", card
        )

        x++;
    }


}

cardBuilder(5)

// cardBuilder()
//###########################//

let change = 0;
let end = 1;
let display = 0;
let inputLength = 0;

document.querySelector('.noLoad').style.display = "none"
document.querySelector('.loadContainer').style.display = "none"
document.querySelectorAll('.job_Card').forEach(jobCard => {
    jobCard.style.display = 'none'
})
document.querySelector('.nextB').style.display = 'none';
document.querySelector('.prevB').style.display = 'none';

document.querySelector('#job-search').addEventListener('click', () => {

    document.querySelector('.main-job').style.backgroundImage = "none"

    document.querySelectorAll('.job_Card').forEach(jobCard => {
        jobCard.style.display = 'none'
    })
    document.querySelector('.nextB').style.display = 'none';
    document.querySelector('.prevB').style.display = 'none';

    document.querySelector('.loadContainer').style.display = "flex"
    document.querySelector('.loadBar').style.width = "2vw"


    //###########################//
    let min = 0;
    let max = 300;
    change = 0;
    end = 1;
//###########################//

    if (end > change) {
        document.querySelector('.nextB').removeAttribute('disabled')
    }
    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }

    let searchValue = document.querySelector('.searchJobs').value;

    //FETCH ALL JOBS
    async function getAllJobs(searchValue) {

        let jobs = new Map();

        const call = await fetch(`https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${searchValue}/United%20States/25/0/DESC/${min}/${max}/1?source=NLx&showFilters=false`, {
            headers: {
                "Authorization": "Bearer " + CAREER_API_KEY,
                Accept: "application/json",
                'Content-Type': 'application/json'
            }
        })

        const reply = await call.json().then(data => {


            for (let job of data.Jobs) {
                if (!jobs.has(job.Company)) {
                    jobs.set(job.Company, job.JvId);
                }
            }

        })

        document.querySelector('.loadBar').style.width = '5vw'
        return jobs;
    }

    const allJobs = getAllJobs(searchValue);
    //###########################//

    // FETCH ALL SPECIFIC JOB CODES
    async function getCodesByJobId(promise) {
        let fetches = [];
        let ids = [];
        let codes = [];
        const p = await promise.then(data => {
            data.forEach((id) => {
                ids.push(id)
            })
        })

        for (let i = 0; i < ids.length; i++) {
            let f = fetch(`https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${ids[i]}?isHtml=true&enableMetaData=true`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            })
            fetches.push(f)
        }
        await Promise.all(fetches)
            .then(values => Promise.all(values.map(v => v.json())))
            .then(data => {
                for (let i = 0; i < data.length; i++) {
                    try {
                        for (let j = 0; j < data[i].OnetCodes.length; j++) {
                            if (!codes.includes(data[i].OnetCodes[j])) {
                                codes.push(data[i].OnetCodes[j])

                            }

                        }
                    } catch (e) {
                        window.scroll(0, 0)
                        document.querySelector('.searchJobs').style.display = "none"
                        document.querySelector('.searchButton').style.display = "none"
                        document.querySelector('.noLoad').style.display = "flex"
                        document.querySelector('.jobMessage').innerHTML = `<p style="text-align: center">OOPS... Something Went Wrong!</p> <br> <p style="text-align: center">Please try a new search.</p>`
                        document.querySelectorAll('.job_Card').forEach(jobCard => {
                            jobCard.style.display = 'none'
                            display++
                            document.querySelector('.noLoadButton').addEventListener('click', () => {
                                document.location.reload()
                            })

                        })
                    }
                }
            })

        document.querySelector('.loadBar').style.width = '15vw'
        return codes;
    }

    const jobCodes = getCodesByJobId(allJobs);
//###########################//


    // FETCH OCCUPATION DATA
    async function getOccData(promise) {

        let values = [];
        let titles_And_Codes = new Map();
        let filteredCodes = [];
        let filteredTitles = [];
        let wages = [];
        let outlooks = [];
        let fetches = [];

        const p = await promise;
        for (let i = 0; i < p.length; i++) {
            let f = fetch(`https://api.careeronestop.org/v1/occupation/${USER_ID}/${p[i]}/0?training=false&interest=true&videos=false&tasks=false&dwas=false&wages=true&alternateOnetTitles=false&projectedEmployment=true&ooh=false&stateLMILinks=false&relatedOnetTitles=false&skills=true&knowledge=false&ability=false&trainingPrograms=true`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            })

            fetches.push(f);
        }

        const call = await Promise.all(fetches)
            .then(values =>
                Promise.all(values.map(async data => await data.json()))).then(data => {

                for (let i = 0; i < data.length; i++) {
                    if (data[i].Message != undefined) {

                    } else {
                        data[i].OccupationDetail.forEach(detail => {
                            if (!titles_And_Codes.has(detail.OnetTitle)) {
                                titles_And_Codes.set(detail.OnetTitle, i)
                            }
                        })
                    }

                }
                titles_And_Codes.forEach((index, title) => {
                    for (let i = 0; i < data[index].OccupationDetail.length; i++) {
                        filteredCodes.push(data[index].OccupationDetail[i].OnetCode)

                        if (data[index].OccupationDetail[i].Wages.NationalWagesList[0].RateType === 'Annual') {
                            wages.push("$" + data[index].OccupationDetail[i].Wages.NationalWagesList[0].Median)
                        } else if (data[index].OccupationDetail[i].Wages.NationalWagesList[1].RateType === 'Annual') {
                            wages.push("$" + data[index].OccupationDetail[i].Wages.NationalWagesList[1].Median)
                        }
                        if (data[index].OccupationDetail[i].BrightOutlookCategory == null) {
                            outlooks.push(data[index].OccupationDetail[i].BrightOutlook)
                        } else {
                            outlooks.push(data[index].OccupationDetail[i].BrightOutlookCategory)
                        }
                    }
                    filteredTitles.push(title);
                })
                end = titles_And_Codes.size;
            })


        values.push(filteredCodes, outlooks, filteredTitles, wages)


        document.querySelector('.loadBar').style.width = '20vw'
        return values;
    }

    const occData = getOccData(jobCodes)
//###########################//

    // FETCH CERTIFICATION DATA
    async function getCerts(promise) {

        let certList = [];
        let fetches = [];

        const p = await promise

        for (let i = 0; i < p[0].length; i++) {

            let f = fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${p[0][i]}/0/0/0/0/0/0/0/0/${min}/${max}`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            })
            fetches.push(f);
        }

        const call = await Promise.all(fetches)
            .then(values =>
                Promise.all(values.map(async data => await data.json())))
            .then(data => {

                for (let i = 0; i < data.length; i++) {
                    certList.push(data[i].CertList)

                }
            })

        document.querySelector('.loadBar').style.width = '35vw'
        return certList;

    }

    const certData = getCerts(occData)
//###########################//

// BUILDER FOR THE HTML
    async function htmlBuilder(certData, occData) {

        const cert = await certData.then(cd => {
            console.log(cd)
            inputLength = cd.length


            if (cd.length <= 0) {


                document.querySelector('.loading').style.display = "none"
                document.querySelector('.loadImg').style.display = "none"
                document.querySelector('#job-input').style.display = "none"
                document.querySelector('#job-search').style.display = "none"
                document.querySelector('.noLoad').style.display = "flex"
                document.querySelector('.noLoadButton').addEventListener('click', () => {
                    document.location.reload()
                })
            }
            let words = searchValue.split(' ');
            document.querySelectorAll('.job_Certs').forEach((c, i) => {
                c.innerHTML = "";
                try {
                    for (let j = 0; j < cd[i + change].length; j++) {

                        if (j < 10) {
                            //Each individual cert seperated by a space
                            console.log(cd[i + change][j])
                            c.innerHTML +=
                                `
                                <p>${cd[i + change][j].Name}</p>
                                <input name="certID" type="hidden" value="${cd[i + change][j].Id}" />
                            `;

                        }
                    }
                } catch (e) {
                    window.scroll(0, 0)
                    document.querySelector('.searchJobs').style.display = "none"
                    document.querySelector('.searchButton').style.display = "none"
                    document.querySelector('.noLoad').style.display = "flex"
                    document.querySelector('.jobMessage').innerHTML = `<p style="text-align: center">OOPS... Something Went Wrong!</p> <br> <p style="text-align: center">Please try a new search.</p>`
                    document.querySelectorAll('.job_Card').forEach(jobCard => {
                        jobCard.style.display = 'none'
                        display++
                        document.querySelector('.noLoadButton').addEventListener('click', () => {
                            document.location.reload()
                        })

                    })

                }
            })

        })

        const occ = await occData.then(od => {

            console.log(od);

            document.querySelectorAll('.job_Title').forEach((title, i) => {
                title.innerHTML = "";
                title.innerHTML = od[2][i + change]

            })

            document.querySelectorAll('.job_Wages').forEach((wage, i) => {
                wage.innerHTML = "";
                wage.innerHTML = od[3][i + change]

            })

            document.querySelectorAll('.job_Outlook').forEach((outlook, i) => {
                outlook.innerHTML = "";
                outlook.innerHTML = od[1][i + change]

            })


        })

        document.querySelector('.loadBar').style.width = '48vw'
        if (document.querySelector('.loadBar').style.width === '48vw') {
            setTimeout(function () {
                document.querySelector('.loadContainer').style.display = "none"

            }, 350)
            let displayTimer = setTimeout(function () {
                document.querySelector(".main-job").style.backgroundImage = "url('/img/backdrop.jpg')"
                document.querySelectorAll('.job_Card').forEach(jobCard => {
                    jobCard.style.display = 'flex'
                })
                document.querySelector('.nextB').style.display = 'block';
                document.querySelector('.prevB').style.display = 'block';
            }, 500)
            if (display > 0) {
                clearTimeout(displayTimer)
            }
        }

    }

    htmlBuilder(certData, occData)
//###########################//

    // NEXT BUTTON CLICK EVENT
    function nextButtonEvent() {
        document.querySelector('.nextB').addEventListener('click', () => {

            window.scroll(0, 0)
            change++

            if (change === 0) {
                document.querySelector('.prevB').setAttribute('disabled', '')
            } else {
                document.querySelector('.prevB').removeAttribute('disabled')
            }

            if (change >= end - 5) {
                document.querySelector('.nextB').setAttribute('disabled', '')
            } else {
                document.querySelector('.nextB').removeAttribute('disabled')
            }

            htmlBuilder(certData, occData)

        })
    }

    nextButtonEvent()
//###########################//

    // PREV BUTTON CLICK EVENT
    function prevButtonEvent() {
        document.querySelector('.prevB').addEventListener('click', () => {
            window.scroll(0, 0)
            change--

            if (change === 0) {
                document.querySelector('.prevB').setAttribute('disabled', '')
            } else {
                document.querySelector('.prevB').removeAttribute('disabled')
            }

            if (change >= end - 5) {
                document.querySelector('.nextB').setAttribute('disabled', '')
            } else {
                document.querySelector('.nextB').removeAttribute('disabled')
            }


            htmlBuilder(certData, occData)

        })
    }

    prevButtonEvent()
//###########################//

})

// SETTING UP THE FORM FOR STORING ROADMAP DATA
document.querySelectorAll('.startRoadMap').forEach((button, i) => {
    // let certs = document.querySelectorAll('.cert_Id')
    // certs.forEach(cert => console.log(cert))
    button.addEventListener('click', () => {
        let company = document.querySelectorAll('.job_Company').item(i)
        let title = document.querySelectorAll('.job_Title').item(i)
        let outlook = document.querySelectorAll('.job_Outlook').item(i)
        let wages = document.querySelectorAll('.job_Wages').item(i)
        let certs = document.querySelectorAll('.job_Certs').item(i)

        // console.log('clicked')
        // console.log(wages)


        document.querySelector('.job_Form').innerHTML +=
            `
                <input type="hidden" name="title" value="${title.innerHTML}" />
<!--                <input type="hidden" name="outlook" value="" />-->
                <input type="hidden" name="wages" value="${wages.innerHTML}" />
                `;

        for (let child of certs.children) {
            if (child.hasAttribute("name")) {
                document.querySelector('.job_Form').innerHTML += `<input type="hidden" name="certs" value="${child.value}" />`
            }
        }


        document.querySelector('.job_Form').submit();
    })
//###########################//
})

//###########################//

