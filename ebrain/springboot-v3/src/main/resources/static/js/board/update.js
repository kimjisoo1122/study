/**
 * 업데이드 폼에서 첨부파일을 삭제처리합니다.
 * 히든폼에 fileId를 첨부하고 전송시 서버에서 최종 삭제를 진행합니다.
 * @param thisElm 해당 첨부파일 엘리먼트
 * @param fileId 해당 첨부파일번호
 */
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