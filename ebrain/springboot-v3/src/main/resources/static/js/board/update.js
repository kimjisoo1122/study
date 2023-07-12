function uploadFile(idx) {
  const fileElm = document.querySelector(`input[type="file"][idx="${idx}"]`);
  const disabledElm = document.querySelector(`input[disabled][idx="${idx}"]`);


  const fileName = fileElm.value.split('/').pop().split('\\').pop();
  disabledElm.value = fileName;

  if (fileElm.files) {
    const fileSize = fileElm.files[0].size;

    if (fileSize / (1024 * 1024) > 10) {
      fileElm.value = '';
      disabledElm.value = '10MB를 넘을 수 없습니다.';
      disabledElm.style.color = 'red';
    } else {
      disabledElm.style.color = 'black';
    }
  }
}
function deleteFile(thisElm, fileId) {
  const fileListElm = document.querySelector('.file-list-container');
  const hiddenInput = document.createElement('input');

  hiddenInput.setAttribute('type', 'hidden');
  hiddenInput.setAttribute('name', 'fileIds');
  hiddenInput.setAttribute('value', fileId);
  fileListElm.appendChild(hiddenInput);

  thisElm.parentElement.remove();

  const fileInputConElm = document.querySelector('.file-input-container')
  const fileInputLen = fileInputConElm.children.length;
  const fileIdx = fileInputLen + 1;

  const inputElm = document.createElement('div');
  inputElm.setAttribute('class', 'file-register-container');
  inputElm.innerHTML =
      `
        <input type="text"
               class="file-disabled"
               idx="${fileIdx}"
               value="" 
               disabled>
        <label for="file${fileIdx}" class="file-input-label">파일 찾기</label>
        <input type="file"
               id="file${fileIdx}"
               idx="${fileIdx}"
               name="files"
               class="file-input"
               onchange="uploadFile(this.getAttribute('idx'))">
      `;

  fileInputConElm.appendChild(inputElm);
}