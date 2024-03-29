
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
}