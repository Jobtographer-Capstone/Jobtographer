document.querySelector('.addBtn').addEventListener('click', () => {

    console.log('click')
    let dates = document.querySelectorAll('.dateValues')
    dates.forEach((date , i) => {

        // console.log(date.value)
        if (date.value === "") {
            date.value = "2099-01-01"

        }
        console.log(date.value)
        document.querySelector('.dates').innerHTML += `<input name="expected" type="hidden" value="${date.value}"/>`
    })
        let f = document.querySelector('.dateForm')
        f.submit()

})

