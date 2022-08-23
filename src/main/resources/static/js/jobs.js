let random = Math.floor(Math.random() * 3) + 1;
let searchValue = '';
if (random === 1) {
    searchValue = "nurse"
}
if (random === 2) {
    searchValue = "cook"
}
if (random === 3) {
    searchValue = "engineer"
}

console.log(searchValue + ": was searched for!");
let codes = [];
let titles = [];
let wages = [];
let outlooks = [];
let jobCerts = [];

let cert1 = [];

let min = 0;
let max = 5;
const apiRequest = () => {

    let url = `https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${searchValue}/United%20States/25/0/DESC/${min}/${max}/1?source=NLx&showFilters=false`;


    fetch(url, {
        headers: {
            "Authorization": "Bearer " + CAREER_API_KEY,
            Accept: "application/json",
            'Content-Type': 'application/json'
        }
    }).then(res => res.json()).then(jobData => {


        // console.log(jobData);
        jobData.Jobs.forEach(job => {


            //DISPLAYS DATA TO JOB COMPANY
            document.querySelectorAll('.job_Company').forEach((company, i) => {
                company.innerHTML = jobData.Jobs[i].Company
            })


            fetch(`https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${job.JvId}?isHtml=true&enableMetaData=true`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            }).then(res => res.json()).then(indiJob => {


                fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${indiJob.OnetCodes[0]}/0/0/0/0/0/0/0/0/${min}/${max}`, {
                    headers: {
                        "Authorization": "Bearer " + CAREER_API_KEY,
                        Accept: "application/json",
                        'Content-Type': 'application/json'
                    }


                }).then(res => res.json()).then(certData => {
                    console.log(certData);
                    //ALL OF THIS BUILDS OUT THE CERT DATA FOR INDEX PAGE CARDS

                    jobCerts.push(certData.CertList)

                    let c1 = [];
                    let c2 = [];
                    let c3 = [];
                    let c4 = [];
                    let c5 = [];

                    for (let i = 0; i < jobCerts[0].length; i++) {
                        c1.push(jobCerts[0][i].Name);
                    }
                    for (let i = 0; i < jobCerts[1].length; i++) {
                        c2.push(jobCerts[1][i].Name);
                    }
                    for (let i = 0; i < jobCerts[2].length; i++) {
                        c3.push(jobCerts[2][i].Name);
                    }
                    for (let i = 0; i < jobCerts[3].length; i++) {
                        c4.push(jobCerts[3][i].Name);
                    }
                    for (let i = 0; i < jobCerts[4].length; i++) {
                        c5.push(jobCerts[4][i].Name);
                    }

                    console.log(c1)
                    console.log(c2)
                    console.log(c3)
                    console.log(c4)
                    console.log(c5)


                    for (let i = 0; i < c1.length; i++) {
                        document.querySelectorAll('.job_Certs').item(0).innerHTML =
                            `<div>${c1}</div>`
                    }
                    for (let i = 0; i < c2.length; i++) {
                        document.querySelectorAll('.job_Certs').item(1).innerHTML =
                            `<div>${c2}</div>`
                    }
                    for (let i = 0; i < c3.length; i++) {
                        document.querySelectorAll('.job_Certs').item(2).innerHTML =
                            `<div>${c3}</div>`
                    }
                    for (let i = 0; i < c4.length; i++) {
                        document.querySelectorAll('.job_Certs').item(3).innerHTML =
                            `<div>${c4}</div>`
                    }
                    for (let i = 0; i < c5.length; i++) {
                        document.querySelectorAll('.job_Certs').item(4).innerHTML =
                            `<div>${c5}</div>`
                    }


                })


                fetch(`https://api.careeronestop.org/v1/occupation/${USER_ID}/${indiJob.OnetCodes[0]}/0?training=false&interest=true&videos=false&tasks=false&dwas=false&wages=true&alternateOnetTitles=false&projectedEmployment=true&ooh=false&stateLMILinks=false&relatedOnetTitles=false&skills=true&knowledge=false&ability=false&trainingPrograms=true`, {
                    headers: {
                        "Authorization": "Bearer " + CAREER_API_KEY,
                        Accept: "application/json",
                        'Content-Type': 'application/json'
                    }
                }).then(res => res.json()).then(occData => {


                    outlooks.push(occData.OccupationDetail[0].BrightOutlook)


                    if (occData.OccupationDetail[0].Wages.NationalWagesList[0].RateType === 'Annual') {
                        wages.push("Median: $" + occData.OccupationDetail[0].Wages.NationalWagesList[0].Median)
                    }
                    if (occData.OccupationDetail[0].Wages.NationalWagesList[1].RateType === 'Annual') {
                        wages.push("Median: $" + occData.OccupationDetail[0].Wages.NationalWagesList[1].Median)
                    }


                    titles.push(occData.OccupationDetail[0].OnetTitle);


                    document.querySelectorAll('.job_Title').forEach((title, i) => {

                        title.innerHTML = titles[i];

                    })

                    document.querySelectorAll('.job_Outlook').forEach((outlook, i) => {
                        outlook.innerHTML = outlooks[i];
                    })

                    document.querySelectorAll('.job_Wages').forEach((wage, i) => {

                        wage.innerHTML = wages[i];

                    })
                })


            })
        })


    });


}
apiRequest()


document.querySelector('.nextB').addEventListener('click', () => {

    min += 5;
    max += 5;

    console.log(min);
    console.log(max);
    apiRequest();

})

document.querySelector('.prevB').addEventListener('click', () => {

    min -= 5;
    max -= 5;

    apiRequest();
})