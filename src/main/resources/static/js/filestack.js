const client = filestack.init(FILESTACK_API_KEY);

const options = {
    transformations: {
        force: true,
        crop: false,
        circle: true,
        rotate: false
    },
    accept: ["image/*"],
    onFileUploadFinished(file) {

        document.querySelector('.myForm').innerHTML += (`<input type="hidden" name="profileImage" value="${file.url}" />`);
        document.querySelector('.myForm').submit();
    }
}

document.querySelector('.edit-img').addEventListener('click', () => {

    client.picker(options).open();
});