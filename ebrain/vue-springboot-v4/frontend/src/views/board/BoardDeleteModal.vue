<!-- 게시글삭제를 처리하는 모달창 컴포넌트 입니다.-->
<template>

  <div class="delete-modal-bg" >

    <div class="delete-modal">

      <form @submit.prevent="submitForm">

        <div class="delete-password-container">
          <div class="delete-password-title">비밀번호
            <span class="required-star">*</span>
          </div>
          <div class="delete-password-input-container">
            <input type="password"
                   v-model="deletePassword"
                   class="delete-password-input">
            <span class="delete-password-input-error">비밀번호를 입력해주세요.</span>
          </div>
        </div>

        <div class="delete-button-container">
          <button type="button" class="delete-button-cancel" @click="$emit('cancelDelete')">취소</button>
          <button type="submit" class="remove-button-submit">확인</button>
        </div>

      </form>

    </div>

  </div>

</template>

<script>
import {deleteBoard} from "@/api/boardService";
import {createSearchQuery} from "@/util/queryparamUtil";

export default {
  name: "BoardDeleteModal",

  data() {
    return {
      deletePassword: ''
    }
  },

  props: {
    condition: Object
  },

  methods: {
    submitForm() {
      console.log(this.deletePassword);
      deleteBoard(this.$route.params.boardId, this.deletePassword)
          .then(() => {
            localStorage.setItem('deleteMsg', '게시글을 삭제하였습니다.');
            this.$router.push(createSearchQuery('/board', this.condition))
          })
          .catch(errorMessage => {
            this.$emit('cancelDelete');
            console.log(errorMessage);
            alert(errorMessage);
          })
    },
  }
}
</script>

<style scoped>

  .delete-modal-bg {
    background-color: rgba(0,0,0,0.4);
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    overflow: hidden;
    z-index: 1;
    width: 100%;
  }

  .delete-modal-bg .required-star {
    color: red;
    margin-bottom: 5px;
  }

  .delete-modal {
    background: white;
    width: 500px;
    border: 1px solid black;
    margin: 15% auto;
    padding: 20px;
  }

  .delete-password-container {
    display: flex;
    border: 1px solid black;
  }

  .delete-password-title {
    align-items: center;
    background: lightgray;
    display: flex;
    font-size: 13px;
    padding-left: 10px;
    width: 100px;
  }

  .delete-password-input-container {
    align-items: center;
    display: flex;
    flex-wrap: wrap;
    flex-grow: 1;
    margin: 5px 0;
  }

  .delete-password-input {
    margin-left: 5px;
    width: 80%;
    padding: 5px;
  }

  .delete-password-input-error {
    color: red;
    display: none;
    font-size: 12px;
    flex-basis: 100%;
    margin-left: 5px;
  }

  .delete-button-container {
    align-items: center;
    display: flex;
    gap: 15px;
    justify-content: center;
    margin-top: 10px;
  }

  .delete-button-container button {
    border: 1px solid black;
    cursor: pointer;
    width: 70px;
  }

  .remove-button-submit {
    background-color: grey;
    color: white;
  }

</style>