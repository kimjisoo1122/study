<!-- 게시글등록 라우터뷰 입니다. -->
<template>

  <form @submit.prevent="submitRegister">

    <div class="register-container">

      <div class="category-container">
        <div class="category-title">
          카테고리
          <span class="required-star">*</span>
        </div>

        <div class="category-select-container">
          <select class="category-select"
                  @change="this.errorFields.categoryId = validCategory(this.categoryId)"
                  v-model="categoryId">
            <CategoryOption/>
          </select>
          <span class="category-select-error">{{ errorFields.categoryId }}</span>
        </div>

      </div>

      <div class="writer-container">
        <div class="writer-title">
          작성자
          <span class="required-star">*</span>
        </div>
        <div class="writer-input-container">
          <input type="text"
                 class="writer-input"
                 @change="this.errorFields.writer = validWriter(this.writer)"
                 v-model="writer">
          <span class="writer-input-error">{{ errorFields.writer }}</span>
        </div>
      </div>

      <div class="password-container">
        <div class="password-title">
          비밀번호
          <span class="required-star">*</span>
        </div>
        <div class="password-input-container">
            <input type="password"
                   class="password-input"
                   @change="this.errorFields.password = validPassword(this.password)"
                   v-model="password">
            <input type="password"
                   class="password-input-confirm"
                   @change="this.errorFields.confirmError = validConfirm(this.password, this.confirmPassword)"
                   v-model="confirmPassword">
            <div class="password-input-error-container">
              <span class="password-input-error">{{ errorFields.password }}</span>
              <span class="password-input-error">{{ errorFields.confirmPassword }}</span>
            </div>

        </div>
      </div>

      <div class="title-container">
        <div class="title-title">
          제목
          <span class="required-star">*</span>
        </div>
        <div class="title-input-container">
          <input type="text"
                 class="title-input"
                 @change="this.errorFields.title = validTitle(this.title)"
                 v-model="title">
          <span class="title-input-error">{{ errorFields.title }}</span>
        </div>
      </div>

      <div class="content-container">
        <div class="content-title">
          내용
          <span class="required-star">*</span>
        </div>
        <div class="content-text-container">
                    <textarea class="content-text"
                              @change="this.errorFields.content = validContent(this.content)"
                              v-model="content">
                    </textarea>
          <span class="content-text-error">{{ errorFields.content }}</span>
        </div>
      </div>

      <div class="file-container">
        <div class="file-title">파일 첨부</div>
        <div class="file-list-container">
          <FileInput v-for="fileId in fineInputSize"
                     :key="fileId"
                     :fileId="fileId"
                     :serverFileError="errorFields.files"
                     @submitFile="this.files.push($event)"/>
        </div>
      </div>

      <div class="button-container">
        <router-link :to="createSearchQuery('/board', condition)">
          <button type="button" class="button-cancel">취소</button>
        </router-link>

        <button type="submit" class="button-save">저장</button>
      </div>

    </div>

  </form>

</template>

<script>
import CategoryOption from "@/components/board/CategoryOption.vue";
import FileInput from "@/components/FileInput.vue";
import {registerBoard} from "@/api/boardService";
import {createCondition, createSearchQuery} from "@/util/queryparamUtil";
import {validCategory, validConfirm, validContent, validPassword, validTitle, validWriter} from "@/util/validUtil";

export default {
  name: "BoardRegister",
  components: {FileInput, CategoryOption},

  data() {
    return {
      condition: {}, // 검색조건
      categoryId: '', // 카테고리
      writer: '', // 작성자
      password: '', // 비밀번호
      confirmPassword: '', // 확인비밀번호
      title: '', // 제목
      content: '', // 내용
      files: [], // 첨부파일,
      fineInputSize: 3,

      errorFields: { // 유효성검증필드
        categoryId: '',
        writer: '',
        password: '',
        confirmPassword: '',
        title: '',
        content: '',
        files: '',
      },

    }
  },

  created() {
    this.condition = createCondition(this.$route.query);
  },

  methods: {
    validConfirm,
    // 등록폼 유효성검증
    validContent,
    validTitle,
    validPassword,
    validCategory,
    validWriter,

    // 게시글목록 돌아가기 쿼리생성
    createSearchQuery,
    /**
     * 게시글등록을 서버에 요청합니다.
     * 성공시 게시글목록, 실패시 유효성에러메시지를 저장합니다.
     */
    submitRegister() {
      if (!this.validForm()) {
        return false;
      }

      const formData = this.createFormData();

      registerBoard(formData)
          .then(boardId => {
            // 게시글상세 페이지로 이동합니다.
            this.$router.push(createSearchQuery(`/board/${boardId}`, this.condition));
          })
          .catch(({data}) => {
            // 유효성검증에 실패한 필드의 에러메시지를 저장합니다.
            for (const field in data) {
              this.errorFields[field] = data[field];
            }
          });
    },

    /**
     * 폼데이터를 검증합니다.
     * @returns {boolean}
     */
    validForm() {
      this.errorFields.categoryId = validCategory(this.categoryId);
      this.errorFields.writer = validWriter(this.writer);
      this.errorFields.password = validPassword(this.password);
      this.errorFields.confirmPassword = validConfirm(this.password, this.confirmPassword);
      this.errorFields.title = validTitle(this.title);
      this.errorFields.content = validContent(this.content);

      let formError = '';
      for (const field in this.errorFields) {
        formError += this.errorFields[field];
      }

      return formError.length === 0;
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
    box-sizing: border-box;
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
    margin: 5px 0 0 10px;
    flex-wrap: wrap;
    gap: 10px;
  }

  .password-input-error-container {
    flex-basis: 100%;
    display: flex;
    flex-direction: column;
    margin-top: -5px;
  }

  .password-input-error{
    color: red;
    font-size: 12px;
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