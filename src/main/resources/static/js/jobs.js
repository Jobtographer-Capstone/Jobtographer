let random = Math.floor(Math.random() * 3) + 1;
let searchValue = '';
if (random === 1) {
    searchValue = "nurse"
}
if (random === 2) {
    searchValue = "cook"
}
if (random === 3) {
    searchValue = "military"
}

console.log(searchValue + ": was searched for!");


let url = `https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${searchValue}/United%20States/25/0/0/0/10/0?source=NLx&showFilters=false`;


fetch(url, {
    headers: {
        "Authorization": "Bearer " + CAREER_API_KEY,
        Accept: "application/json",
        'Content-Type': 'application/json'
    }
}).then(res => res.json()).then(jobData => {



    jobData.Jobs.forEach(job => {

        fetch(`https://api.careeronestop.org/v1/jobsearch/${USER_ID}/${job.JvId}?isHtml=true&enableMetaData=true`, {
            headers: {
                "Authorization": "Bearer " + CAREER_API_KEY,
                Accept: "application/json",
                'Content-Type': 'application/json'
            }
        }).then(res=>res.json()).then(indiJob => {

            fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${indiJob.OnetCodes[0]}/0/0/0/0/0/0/0/0/0/10`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            }).then(res => res.json()).then(certData => {

                console.log(certData);

            })


            fetch(`https://api.careeronestop.org/v1/occupation/${USER_ID}/${indiJob.OnetCodes[0]}/0?training=false&interest=true&videos=false&tasks=false&dwas=false&wages=true&alternateOnetTitles=false&projectedEmployment=true&ooh=false&stateLMILinks=false&relatedOnetTitles=false&skills=true&knowledge=false&ability=false&trainingPrograms=true`, {
                headers: {
                    "Authorization": "Bearer " + CAREER_API_KEY,
                    Accept: "application/json",
                    'Content-Type': 'application/json'
                }
            }).then(res => res.json()).then(occData => {

                console.log(occData);


            })

        })


    })


});





