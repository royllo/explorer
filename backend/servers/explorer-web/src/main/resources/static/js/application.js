function copy(textAreaId) {
    let textarea = document.getElementById(textAreaId);
    navigator.clipboard.writeText(textarea.value).then(value => {
    });
}