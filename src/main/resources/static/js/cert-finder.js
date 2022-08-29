console.log('linked cert-finder')

let USER_ID = `3d2MyBdqKletjEp`;
let CAREER_API_KEY = `4Gi0dKbERiZIOczLf73Fk2vis+cVA1Zl900fzqdAKVHQtnCwzroTfAvLgerN1KU2XNQLYGPPosqjBkLQj41/ng==`;

async function getCertById(id){
    const call = fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${id}`, {
        headers: {
            "Authorization": "Bearer " + CAREER_API_KEY,
            Accept: "application/json",
            'Content-Type': 'application/json'
        }
    })
    return (await call).json()
}

document.querySelector('.testB').addEventListener('click', ()=>{

    let id = document.querySelector('.testI').value;
    console.log(id);
    const cert = getCertById(id);
    console.log(cert);

})