/**
 * 게시글의 댓글을 등록합니다.
 * @param boardId 게시글 번호
 */
function registerReply(boardId) {
  const inputElm = document.querySelector('.reply-register-input');
  const replyContent = inputElm.value;

  const xhr = new XMLHttpRequest();
  xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        const replyDto = JSON.parse(xhr.responseText);
        const containerElm = document.querySelector('.reply-list-container');

        const reply =
            `
            <div class="reply">
              <div class="reply-date">${replyDto.formattedCreateDate}</div>
              <div class="reply-content">${replyDto.replyContent}</div>
            </div>
            `

        containerElm.insertAdjacentHTML('beforeend', reply);
      } else {
        // 등록에 실패하면 에러메시지를 받습니다.
        console.error(xhr.responseText);
      }
      inputElm.value = '';
    }
  }

  xhr.open('POST', '/reply/register', true);
  xhr.setRequestHeader("Content-Type", "application/json")

  const registerReplyDto = {
    boardId: boardId,
    replyContent: replyContent,
  }

  xhr.send(JSON.stringify(registerReplyDto));
}

/**
 * 삭제 모달창을 닫습니다.
 */

function removeCancel() {
  const modalElm = document.querySelector('.remove-modal-bg');
  modalElm.style.display = 'none';
}

/**
 * 삭제 모달창을 엽니다.
 */
function removeOpen() {
  const modalElm = document.querySelector('.remove-modal-bg');
  modalElm.style.display = 'block';
}

/**
 * 삭제 모달창의 입력된 비밀번호의 빈 값을 체크합니다.
 * @returns 입력된 값이 존재하면 true
 */
function validRemovePassword() {
  const inputElm = document.querySelector('.remove-password-input');
  const errElm = document.querySelector('.remove-password-input-error');
  if (inputElm.value === '') {
    errElm.style.display = 'block';
    inputElm.style.border = '1px solid red';
    return false;
  } else {
    errElm.style.display = 'none';
    inputElm.style.border = '1px solid black';
    return true;
  }
}
