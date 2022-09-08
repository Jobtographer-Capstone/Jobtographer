"use strict";
let USER_ID = `3d2MyBdqKletjEp`;
let CAREER_API_KEY = `4Gi0dKbERiZIOczLf73Fk2vis+cVA1Zl900fzqdAKVHQtnCwzroTfAvLgerN1KU2XNQLYGPPosqjBkLQj41/ng==`;

async function getCertById(id) {
    const call = fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${id}`, {
        headers: {
            "Authorization": "Bearer " + CAREER_API_KEY,
            Accept: "application/json",
            'Content-Type': 'application/json'
        }
    })
    return (await call).json()
}

let vals = document.querySelectorAll(".cert-name")
console.log(vals);
for (let val of vals) {
    console.log(val.innerText);
    getCertById(val.innerText).then(data => {
        console.log(data);
        val.replaceWith(data.Name)
    })
}