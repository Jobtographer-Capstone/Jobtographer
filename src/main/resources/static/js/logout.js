"use strict";

document.querySelector("#logout").addEventListener("click", () => {
    let trueFalse = confirm("Are you sure you want to logout ?");
    console.log(trueFalse)
    if (trueFalse === true) {
        window.location.assign("/logout");
        alert("You have been logged out");
        window.location.assign("/login");
    }
});

document.querySelector("#deleteAccount").addEventListener("click",()=>{
    let trueFalse = confirm("Are you sure you want to delete your account ?");
    if(trueFalse){
        window.location.assign("/delete/user");
        alert("Account deleted!");
    }
});
