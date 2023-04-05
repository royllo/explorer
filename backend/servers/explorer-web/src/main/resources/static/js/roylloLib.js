function copy(textAreaId) {
    let textarea = document.getElementById(textAreaId);
    textarea.select();
    document.execCommand("copy");
}