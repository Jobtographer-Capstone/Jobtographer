// THESE ARE FOR SEARCH VALUES
let min = 0;
let max = 5;
let searchValue = "engineer";
let page = 0;
//###########################//

let certList = [];
let jobs = new Map();
let codes = [];

let titles = [];
let companies = [];
let wages = [];
let outlooks = [];

//SET LOAD BUTTONS TO DISPLAY NONE ON PAGE LOAD
document.querySelector('.loadMore').style.display = "none";
document.querySelector('.loadPrev').style.display = "none";
//###########################//

// THIS IS FOR CHANGING DATA THAT IS DISPLAYED
let change = 0;
let end = 1;
//###########################//

//###########################//

if (change === 0) {
    document.querySelector('.prevB').setAttribute('disabled', '')
} else {
    document.querySelector('.prevB').removeAttribute('disabled')
}

if (change < end) {
    document.querySelector('.nextB').removeAttribute('disabled')
}
//###########################//


//SEARCH BUTTON CLICK EVENT
document.querySelector('.searchButton').addEventListener('click', () => {

    end = 1;
    change = 0;

    if (end > change) {
        document.querySelector('.nextB').removeAttribute('disabled')
    }
    end = 0;
    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }


    searchValue = document.querySelector('.searchJobs').value;
    const allJobs = getAllJobs();
    const jobCodes = getCodesByJobId(allJobs);
    const occData = getOccData(jobCodes)
    const certData = getCerts(jobCodes)
    htmlBuilder(allJobs, occData, certData)


    // NEXT BUTTON CLICK EVENT
    document.querySelector('.nextB').addEventListener('click', () => {


        change++


        if (change === 0) {
            document.querySelector('.prevB').setAttribute('disabled', '')
        } else {
            document.querySelector('.prevB').removeAttribute('disabled')
        }

        console.log("CHANGE: " + change)
        console.log("END: " + end)
        if ((end - 2) > change) {
            document.querySelector('.nextB').removeAttribute('disabled')
        }
        if (change >= (end - 2)) {
            document.querySelector('.nextB').setAttribute('disabled', '')
        }
        awaitNewLoad();
        htmlBuilder(allJobs, occData, certData)
    })
//###########################//


    // PREV BUTTON CLICK EVENT
    document.querySelector('.prevB').addEventListener('click', () => {

        change--

        console.log("CHANGE: " + change)
        console.log("END: " + end)

        if (change === 0) {
            document.querySelector('.prevB').setAttribute('disabled', '')
        } else {
            document.querySelector('.prevB').removeAttribute('disabled')
        }

        if ((end - 2) > change) {
            document.querySelector('.nextB').removeAttribute('disabled')
        }
        if (change >= (end - 2)) {
            document.querySelector('.nextB').setAttribute('disabled', '')
        }

        htmlBuilder(allJobs, occData, certData)
    })
})
//###########################//


//FETCH ALL JOBS
async function getAllJobs() {

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
        end = jobs.size

    })


    return jobs;
}
const allJobs = getAllJobs();
//###########################//


// FETCH ALL SPECIFIC JOB CODES
async function getCodesByJobId(promise) {
    let fetches = [];
    let ids = [];

    const p = await promise.then(data => {
        data.forEach((id, name) => {
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
                codes.push(data[i].OnetCodes[0])
            }
        })
    return codes;
}
const jobCodes = getCodesByJobId(allJobs);
//###########################//


// FETCH OCCUPATION DATA
async function getOccData(promise) {
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
            Promise.all(values.map(async data => await data.json())))

    return call
}
const occData = getOccData(jobCodes)
//###########################//


// FETCH CERTIFICATION DATA
async function getCerts(promise) {
    let fetches = [];
    const p = await promise

    for (let i = 0; i < p.length; i++) {
        let f = fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${p[i]}/0/0/0/0/0/0/0/0/${min}/${max}`, {
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

    return certList;

}

const certData = getCerts(jobCodes)

//###########################//


// BUILDER FOR THE HTML
async function htmlBuilder(jobData, occData, certData) {




    const jobs = await jobData.then(jd => {

        jd.forEach((id, company) => {
            if (company !== undefined) {
                companies.push(company);
            }
        })

    })

    document.querySelectorAll('.job_Company').forEach((company, i) => {
        company.innerHTML = companies[i + change]
    })

    const occ = await occData.then(od => {
        for (let i = 0; i < od.length; i++) {

            if (od[i].Message != undefined) {
                wages.push('unavailable')
            } else if (od[i].OccupationDetail[0].Wages.NationalWagesList[0].RateType === 'Annual') {
                wages.push("$" + od[i].OccupationDetail[0].Wages.NationalWagesList[0].Median)
            } else if (od[i].OccupationDetail[0].Wages.NationalWagesList[1].RateType === 'Annual') {
                wages.push("$" + od[i].OccupationDetail[0].Wages.NationalWagesList[1].Median)
            }

        }
        for (let i = 0; i < od.length; i++) {
            if (od[i].Message != undefined) {
                // wages.push('unavailable')
                titles.push('unavailable')
            } else {
                titles.push(od[i].OccupationDetail[0].OnetTitle)
            }

        }
        for (let i = 0; i < od.length; i++) {
            if (od[i].Message != undefined) {
                outlooks.push('unavailable')
            } else if (od[i].OccupationDetail[0].BrightOutlookCategory == null) {
                outlooks.push(od[i].OccupationDetail[0].BrightOutlook)
            } else {
                outlooks.push(od[i].OccupationDetail[0].BrightOutlookCategory)
            }
        }


        document.querySelectorAll('.job_Title').forEach((title, i) => {
            title.innerHTML = titles[i + change]
        })

        document.querySelectorAll('.job_Wages').forEach((wage, i) => {
            wage.innerHTML = wages[i + change]
        })

        document.querySelectorAll('.job_Outlook').forEach((outlook, i) => {
            outlook.innerHTML = outlooks[i + change]
        })
    })


    const cert = await certData.then(cd => {
        console.log(cd)

        document.querySelectorAll('.job_Certs').forEach((c, i) => {
            c.innerHTML = "";
            console.log(i + change)
            for (let j = 0; j < cd[i + change].length; j++) {

                if (j < 5) {
                    //Each individual cert seperated by a space
                    c.innerHTML += cd[i + change][j].Id + " "
                }
            }

        })


    })

}


let x = 0;
while (x < 3) {
    let card = '<div class=" col-lg-4 job_Card">' +
        '<p class="job_Title" th:name="title"></p>' +
        'Average salary: ' +
        '<p class="job_Wages" th:name="wages"></p>' +
        '<p class="job_Certs cert-name" th:name="certs"></p>' +
        '<button class="startRoadMap btn btn-primary" type="button">Create RoadMap</button>' +
        '</div>';
    document.querySelector(".populate").insertAdjacentHTML("beforeEnd", card
    )
    x++;
}


htmlBuilder(allJobs, occData, certData)
//###########################//


// SETTING UP THE FORM FOR STORING ROADMAP DATA
document.querySelectorAll('.startRoadMap').forEach((button, i) => {
    let company = document.querySelectorAll('.job_Company').item(i)
    let title = document.querySelectorAll('.job_Title').item(i)
    let outlook = document.querySelectorAll('.job_Outlook').item(i)
    let wages = document.querySelectorAll('.job_Wages').item(i)
    let certs = document.querySelectorAll('.job_Certs').item(i)

    button.addEventListener('click', () => {

        document.querySelector('.job_Form').innerHTML +=
            `
                <input type="hidden" name="company" value="${company.innerHTML}" />
                <input type="hidden" name="title" value="${title.innerHTML}" />
                <input type="hidden" name="outlook" value="${outlook.innerHTML}" />
                <input type="hidden" name="wages" value="${wages.innerHTML}" />
                <input type="hidden" name="certs" value="${certs.innerHTML}" />
            `;

        document.querySelector('.job_Form').submit();
    })
//###########################//
})



// NEXT BUTTON CLICK EVENT
document.querySelector('.nextB').addEventListener('click', () => {

    change++

    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }

    if (change >= (end - 2)) {
        document.querySelector('.nextB').setAttribute('disabled', '')
    } else {
        document.querySelector('.nextB').removeAttribute('disabled')
    }
    awaitNewLoad();
    htmlBuilder(allJobs, occData, certData)
})
//###########################//


// PREV BUTTON CLICK EVENT
document.querySelector('.prevB').addEventListener('click', () => {

    change--

    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }

    if (change >= (end - 2)) {
        document.querySelector('.nextB').setAttribute('disabled', '')
    } else {
        document.querySelector('.nextB').removeAttribute('disabled')
    }

    htmlBuilder(allJobs, occData, certData)
})
//###########################//


// SETS POPUP TO LOAD NEW JOB DATA
function awaitNewLoad() {
    let target = document.querySelector('.nextB')

    if (target.hasAttribute('disabled')) {
        document.querySelector('#populate').className = "nextPop";

        document.querySelectorAll('.startRoadMap').forEach(b =>{
            b.style.display = "none";
        })

        document.querySelectorAll('.job_Card').forEach(c => {
            c.style.display = "none";
        })

        document.querySelector('.nextB').style.display = "none";
        document.querySelector('.prevB').style.display = "none";
        document.querySelector('.loadMore').style.display = "block";
        document.querySelector('.loadPrev').style.display = "block";

    } else{

        let popup = document.querySelector('.nextPop')
        if (popup !== null){
            document.querySelector('.jobs').className -= "nextPop";
        }

        document.querySelectorAll('.startRoadMap').forEach(b =>{
            b.style.display = "block";
        })

        document.querySelectorAll('.job_Card').forEach(c => {
            c.style.display = "block";
        })

        document.querySelector('.nextB').style.display = "block";
        document.querySelector('.prevB').style.display = "block";

        document.querySelector('.loadMore').style.display = "none";
        document.querySelector('.loadPrev').style.display = "none";
    }

}
//###########################//

// BUTTON EVENTS FOR POPUP
document.querySelector('.loadPrev').addEventListener('click', ()=>{

    certList = [];
    jobs = new Map();
    codes = [];

    titles = [];
    companies = [];
    wages = [];
    outlooks = [];

    console.log('GOING BACKWARDS!')
    change = 0;
    end = 1;
    page --;


    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }

    if (change < end) {
        document.querySelector('.nextB').removeAttribute('disabled')
    }

    if (page >= -1) {

        let popup = document.querySelector('.nextPop')
        if (popup !== null){
            popup.classList.remove('nextPop')
        }

        document.querySelector('.loadMore').style.display = "none";
        document.querySelector('.loadPrev').style.display = "none";

        document.querySelector('.nextB').style.display = "block";
        document.querySelector('.prevB').style.display = "block";

        document.querySelectorAll('.startRoadMap').forEach(b =>{
            b.style.display = "block";
        })

        document.querySelectorAll('.job_Card').forEach(c => {
            c.style.display = "block";
        })
        min -= 5;
        max -= 5;


        const allJobs = getAllJobs();
        const jobCodes = getCodesByJobId(allJobs);
        const occData = getOccData(jobCodes)
        const certData = getCerts(jobCodes)
        htmlBuilder(allJobs, occData, certData)
    }

    if (page <= 0) {
        document.querySelector('.loadPrev').setAttribute('disabled', '')
    } else{
        document.querySelector('.loadPrev').removeAttribute('disabled')
    }
})

document.querySelector('.loadMore').addEventListener('click', ()=>{

    // certList = [];
    jobs = new Map();
    codes = [];

    titles = [];
    companies = [];
    wages = [];
    outlooks = [];

    change = 0;
    end = 1;
    page ++;

    if (change === 0) {
        document.querySelector('.prevB').setAttribute('disabled', '')
    } else {
        document.querySelector('.prevB').removeAttribute('disabled')
    }

    if (change < end) {
        document.querySelector('.nextB').removeAttribute('disabled')
    }

    let popup = document.querySelector('.nextPop')
    if (popup !== null){
        popup.classList.remove('nextPop')
    }

    if (page <= 0) {
        document.querySelector('.loadPrev').setAttribute('disabled', '')
    } else{
        document.querySelector('.loadPrev').removeAttribute('disabled')
    }

    document.querySelector('.loadMore').style.display = "none";
    document.querySelector('.loadPrev').style.display = "none";

    document.querySelector('.nextB').style.display = "block";
    document.querySelector('.prevB').style.display = "block";

    document.querySelectorAll('.startRoadMap').forEach(b =>{
        b.style.display = "block";
    })

    document.querySelectorAll('.job_Card').forEach(c => {
        c.style.display = "block";
    })

    min += 5;
    max += 5;


    const allJobs = getAllJobs();
    const jobCodes = getCodesByJobId(allJobs);
    const occData = getOccData(jobCodes)
    const certData = getCerts(jobCodes)
    htmlBuilder(allJobs, occData, certData)
})
//###########################//

if (page <= 0) {
    document.querySelector('.loadPrev').setAttribute('disabled', '')
} else{
    document.querySelector('.loadPrev').removeAttribute('disabled')
}
