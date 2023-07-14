/**
 * 업로드파일의 유효성을 검증합니다.
 * @param idx 업로드할 파일의 input index
 */
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