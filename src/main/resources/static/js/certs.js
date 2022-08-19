document.querySelector('.cert_search').addEventListener('click', () => {


    document.querySelector('.card_holder').innerHTML = "";
    const searchValue = document.querySelector('.cert_value').value;
    console.log(searchValue + " was searched for!")


    fetch(`https://api.careeronestop.org/v1/certificationfinder/${USER_ID}/${searchValue}/0/0/0/0/0/0/0/0/0/10`, {
        headers: {
            "Authorization": "Bearer " + CAREER_API_KEY,
            Accept: "application/json",
            'Content-Type': 'application/json'
        }
    }).then(res => res.json()).then(data => {
        console.log(data);

        data.CertList.forEach((cert, i) => {

                document.querySelector('.card_holder').innerHTML += `<div class="card m-2 col-4 justify-content-center"> ${cert.Name}
            <br> ${cert.Id} 
            <br>
<!--            <a href="${cert.Url}">Click Me</a>-->
            <input type="hidden" value="${cert.Id}" class="cert_id">
            
    
    <button class="add_Cert btn btn-primary w-50 m-auto" type="button"  data-toggle="modal" data-target="#addCert">Select</button>
    

                  

            
            
            
            
<!--            <button type="button" class="by_id" value="${cert.Id}">View Cert</button>-->
            </div>`
            }
        )


        for (let i = 0; i < data.CertList.length; i++) {
            let c = document.querySelector('.card_holder').children;
            c.item(i).children.item(3).addEventListener('click', () => {


                console.log('cert add clicked!')
                document.querySelector('.certForm').innerHTML += `<input type="hidden" id="cert-name" name="certificationName" th:field="*{certificationName}" value="${data.CertList[i].Name}"> <input type="hidden" name="id" th:field="*{id}">
                    `
                // let date = document.querySelector("#exp-date").value;
                // console.log(date);

                // document.querySelector('.certForm').submit();

            })
        }


    })
})