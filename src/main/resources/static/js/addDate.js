document.querySelector('.addBtn').addEventListener('click', () => {

    console.log('click')
    let dates = document.querySelectorAll('.dateValues')
    dates.forEach((date, i) => {

        if (document.querySelectorAll('.testing').item(i).value !== "" && date.value === "") {
            date.value = document.querySelectorAll('.testing').item(i).value
        }

        if (date.value === "") {
            date.value = "2099-01-01"
        }

        document.querySelector('.dates').innerHTML += `<input name="expected" type="hidden" value="${date.value}"/>`
    })
    let f = document.querySelector('.dateForm')
    f.submit()

})

