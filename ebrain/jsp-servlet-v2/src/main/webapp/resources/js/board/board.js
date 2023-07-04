function replyRegister(boardId) {
  const inputElm = document.querySelector('.reply-register-input');
  const content = inputElm.value;

  const xhr = new XMLHttpRequest();
  xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        const replyDto = JSON.parse(xhr.responseText);
        const containerElm = document.querySelector('.reply-list-container');

        const formatCreateDate = formatDate(replyDto.createDate);
        const reply =
            `
            <div class="reply">
              <div class="reply-date">${formatCreateDate}</div>
              <div class="reply-content">${replyDto.content}</div>
            </div>
            `

        containerElm.insertAdjacentHTML('beforeend', reply);

        inputElm.value = '';
      } else {
        console.error('댓글 등록 실패');
      }
    }
  }

  xhr.open('POST', '/board/replyRegister', true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")

  xhr.send("boardId=" + encodeURIComponent(boardId) + "&content=" + encodeURIComponent(content));
}

function formatDate(localDateTime) {
  const date = localDateTime.date;
  const time = localDateTime.time;
  const year = date.year;
  const month = date.month.toString().padStart(2, '0');
  const day = date.day.toString().padStart(2, '0');
  const hours = time.hour.toString().padStart(2, '0');
  const minute = time.minute.toString().padStart(2, '0');

  return year + '.' + month + '.' + day + ' ' + hours + ':' + minute;
}

function removeCancel() {
  const modalElm = document.querySelector('.remove-modal-bg');
  modalElm.style.display = 'none';
}

function removeOpen() {
  const modalElm = document.querySelector('.remove-modal-bg');
  modalElm.style.display = 'block';
}

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
