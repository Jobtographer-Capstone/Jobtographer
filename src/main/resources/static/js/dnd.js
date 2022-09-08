document.querySelectorAll(".cert-drag").forEach((cert)=>{
    cert.addEventListener("dragstart", dragstart_handler)
})

function dragstart_handler(ev) {
    // Add the target element's id to the data transfer object
    ev.dataTransfer.setData("application/my-app", ev.target.id);
    ev.dataTransfer.effectAllowed = "move";
}

document.querySelectorAll(".dropzone").forEach((drop) =>{
    drop.addEventListener("dragover",dragover_handler);
})

function dragover_handler(ev) {
    ev.preventDefault();
    ev.dataTransfer.dropEffect = "move"
}

function drop_handler(ev) {
    ev.preventDefault();
    // Get the id of the target and add the moved element to the target's DOM
    const data = ev.dataTransfer.getData("application/my-app");
    ev.target.appendChild(document.getElementById(data));
}
