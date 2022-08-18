"use strict";

document.getElementById("logout").addEventListener("click", () => {
    let trueFalse = confirm("Are you sure you want to logout ?");
    console.log(trueFalse)
    if (trueFalse === true) {
        window.location.assign("/logout");
        window.location.assign("/login");
    }
});
