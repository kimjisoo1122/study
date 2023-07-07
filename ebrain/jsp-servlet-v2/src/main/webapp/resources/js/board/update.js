
function uploadFile(thisElm) {
  const fileName = thisElm.value.split('/').pop().split('\\').pop();
  const disabledElm = thisElm.parentElement.querySelector('input[disabled]');
  disabledElm.value = fileName;

  // 파일 사이즈 체크
  if (thisElm.files) {
    const fileSize = thisElm.files[0].size;

    if (fileSize / (1024 * 1024) > 10) {
      thisElm.value = '';
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
  hiddenInput.setAttribute('name', 'fileId');
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
               value="" 
               disabled>
        <label for="file${fileIdx}" class="file-input-label">파일 찾기</label>
        <input type="file"
               id="file${fileIdx}"
               name="file${fileIdx}"
               class="file-input"
               onchange="uploadFile(this)">
      `;

  fileInputConElm.appendChild(inputElm);
}