<!-- 게시글등록 라우터뷰 입니다. -->
<template>

  <form @submit.prevent="register">

    <div class="register-container">

      <div class="category-container">
        <div class="category-title">카테고리
          <span class="required-star">*</span>
        </div>

        <div class="category-select-container">
          <select class="category-select"
                  v-model="categoryId">
            <CategoryOption/>
          </select>
          <span class="category-select-error"></span>
        </div>

      </div>

      <div class="writer-container">
        <div class="writer-title">작성자
          <span class="required-star">*</span>
        </div>
        <div class="writer-input-container">
          <input type="text"
                 class="writer-input"
                 v-model="writer">
          <span class="writer-input-error"></span>
        </div>
      </div>

      <div class="password-container">
        <div class="password-title">비밀번호
          <span class="required-star">*</span>
        </div>
        <div class="password-input-container">
          <input type="password"
                 class="password-input"
                 v-model="password">
          <input type="password"
                 class="password-input-confirm"
                 v-model="confirmPassword">
          <span class="password-input-error"></span>
        </div>
      </div>

      <div class="title-container">
        <div class="title-title">제목
          <span class="required-star">*</span>
        </div>
        <div class="title-input-container">
          <input type="text"
                 class="title-input"
                 v-model="title">
          <span class="title-input-error"></span>
        </div>
      </div>

      <div class="content-container">
        <div class="content-title">내용
          <span class="required-star">*</span>
        </div>
        <div class="content-text-container">
                    <textarea class="content-text"
                              v-model="content">
                    </textarea>
          <span class="content-text-error"></span>
        </div>
      </div>

      <div class="file-container">
        <div class="file-title">파일 첨부</div>
        <div class="file-list-container">
          <FileInput v-for="fileId in 3"
                     :key="fileId"
                     :fileId="fileId"
                     @submitFile="this.files.push($event)"/>
        </div>
      </div>

      <div class="button-container">
        <router-link :to="createSearchQuery('/board', condition)">
          <button type="button" class="button-cancel">취소</button>
        </router-link>

        <button type="submit"
                class="button-save">
          저장
        </button>
      </div>

    </div>

  </form>

</template>

<script>
import CategoryOption from "@/components/CategoryOption.vue";
import FileInput from "@/components/FileInput.vue";
import {registerBoard} from "@/api/boardService";
import {createCondition, createSearchQuery} from "@/util/queryparamUtil";

export default {
  name: "BoardRegister",
  components: {FileInput, CategoryOption},

  data() {
    return {
      condition: {},
      categoryId: '',
      writer: '',
      password: '',
      confirmPassword: '',
      title: '',
      content: '',
      files: [],
    }
  },

  methods: {
    createSearchQuery,
    /**
     * 게시글등록을 서버에 요청합니다.
     * 성공시 게시글목록, 실패시 유효성에러를 띄웁니다.
     * //TODO 유효성검증 설정
     */
    register() {
      const formData = this.createFormData();

      registerBoard(formData)
          .then(boardId => {
            // 게시글상세 페이지로 이동합니다.
            this.$router.push(createSearchQuery(`/board/${boardId}`, this.condition));
          })
          .catch(errorField => {
            console.log(errorField);
          });
    },

    /**
     * FileInput 컴포넌트에서 이벤트발송된 파일을 배열로 생성합니다.
     * @param file 파일객체
     */
    addFiles(file) {
      this.files.push(file)
    },

    /**
     * FormData를 생성합니다
     * @returns FormData 게시글form정보
     */
    createFormData() {
      const formData = new FormData();
      formData.append('categoryId', this.categoryId);
      formData.append('writer', this.writer);
      formData.append('password', this.password);
      formData.append('title', this.title);
      formData.append('content', this.content);

      this.files.forEach(file => {
        formData.append('files', file)
      });

      return formData;
    },

  },

  created() {
    this.condition = createCondition(this.$route.query);
  }
}
</script>

<style scoped>

  .required-star {
    color: tomato;
    margin-bottom: 3px;
  }

  [class$="title"] {
    align-items: center;
    background-color: lightgray;
    display: flex;
    padding: 10px;
    width: 150px;
    font-size: 14px;
    box-sizing: border-box;
  }

  input {
    height: 30px;
    padding: 0 5px;
    width: 200px;
  }

  .category-container {
    border-top: 1px solid black;
    display: flex;
  }

  .category-select-container {
    align-items: center;
    display: flex;
    flex-wrap: wrap;
    margin-left: 10px;
  }

  .category-select {
    height: 30px;
    padding: 0 5px;
    width: 200px;
    margin: 5px 0;
  }

  .category-select-error {
    color: red;
    flex-basis: 100%;
    font-size: 12px;
  }

  .writer-container {
    border-top: 1px solid black;
    display: flex;
  }

  .writer-input-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-left: 10px;
  }

  .writer-input {
    margin: 5px 0;
  }

  .writer-input-error {
    color: red;
    font-size: 12px;
  }

  .password-container {
    border-top: 1px solid black;
    display: flex;
  }

  .password-input-container {
    align-items: center;
    display: flex;
    margin-left: 10px;
    flex-wrap: wrap;
  }

  .password-input {
    margin: 5px 0;
  }

  .password-input-confirm {
    margin: 5px 0 5px 10px;
  }

  .password-input-error {
    color: red;
    font-size: 12px;
    flex-basis: 100%;
  }

  .title-container {
    border-top: 1px solid black;
    display: flex;
  }

  .title-input-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-left: 10px;
  }

  .title-input {
    margin: 5px 0;
  }

  .title-input-error {
    color: red;
    font-size: 12px;
  }

  .content-container {
    border-top: 1px solid black;
    display: flex;
  }

  .content-text-container {
    display: flex;
    flex-direction: column;
    margin-left: 10px;
  }

  .content-text {
    height: 150px;
    margin: 5px 0;
    padding: 10px;
    resize: none;
    width: 800px;
  }

  .content-text-error {
    color: red;
    font-size: 12px;
    margin: 5px 0;
  }

  .file-container {
    border-top: 1px solid black;
    border-bottom: 1px solid black;
    display: flex;
  }

  .file-list-container {
    margin-left: 10px;
  }

  .button-container {
    align-items: center;
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
  }

  .button-container button {
    border: none;
    cursor: pointer;
    padding: 5px 10px;
    width: 70px;
  }

  .button-save {
    background-color: lightgray;
  }

</style>