<!-- 게시글 업데이트폼 컴포넌트 입니다.-->
<template>

  <form @submit.prevent="submitUpdate">

    <div class="update-container">

      <div class="category-container">
        <div class="category-title">
          카테고리
          <span class="required-star">*</span>
        </div>
        <div class="category-select-container">
          <select class="category-select"
                  @change="this.errorFields.categoryId = validCategory(this.categoryId)"
                  v-model="categoryId">
            <!-- 카테고리옵션 -->
            <CategoryOption :selectedId="categoryId"/>
          </select>
          <span class="category-select-error">{{ errorFields.categoryId }}</span>
        </div>
      </div>

      <div class="create-date-container">
        <div class="create-title">등록 일시</div>
        <div class="create-date">{{ board.createDate }}</div>
      </div>

      <div class="update-date-container">
        <div class="update-title">수정 일시</div>
        <div class="update-date">{{ board.updateDate }}</div>
      </div>

      <div class="view-container">
        <div class="view-title">조회수</div>
        <div class="view-cnt">{{ board.viewCnt }}</div>
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
        </div>
        <div class="password-input-container">
          <input type="password"
                 class="password-input"
                 placeholder="비밀번호"
                 v-model="password">
          <span class="password-input-error">{{ errorFields.password }}</span>
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
          내용<span class="required-star">*</span>
        </div>
        <div class="content-text-container">
                <textarea class="content-text"
                          @change="this.errorFields.content = validContent(this.content)"
                          v-model="content">
                </textarea>
          <span class="content-text-error">{{ errorFields.content }}</span>
        </div>
      </div>


      <div class="file-list-container">
        <div class="file-title">파일 첨부</div>
        <div class="board-update-file-input-container">

          <!-- 첨부파일목록 -->
          <div v-for="(file, i) in files" :key="i">
            <div class="file-container" v-if="!hideFile[i]">
              <BoardFile :file="file"/>
              <button type="button"
                      class="file-delete-btn"
                      @click="deleteFile(file.fileId, i)">
                X
              </button>
            </div>
          </div>

          <!-- 파일인풋 -->
          <div class="file-register-container">
            <FileInput v-for="fileId in fileInputSize"
                       :key="fileId"
                       :fileId="fileId"
                       :serverFileError="errorFields.files"
                       @submitFile="this.saveFiles.push($event)"/>

          </div>
        </div>
      </div>

      <div class="button-container">
        <button type="button" class="button-cancel" @click="$emit('cancelUpdate')">취소</button>
        <button type="submit" class="button-save">저장</button>
      </div>

    </div>

  </form>

</template>

<script>
import CategoryOption from "@/components/board/CategoryOption.vue";
import BoardFile from "@/components/board/BoardFile.vue";
import FileInput from "@/components/FileInput.vue";
import {updateBoard} from "@/api/boardService";
import {validCategory, validConfirm, validContent, validPassword, validTitle, validWriter} from "@/util/validUtil";

export default {
  name: "BoardUpdate",
  components: {FileInput, BoardFile, CategoryOption},

  data() {
    return {
      categoryId: '', // 카테고리
      writer: '', // 작성자
      password: '', // 비밀번호
      title: '', // 제목
      content: '', // 내용

      // 유효성검증
      errorFields: { // 유효성검증필드
        categoryId: '',
        writer: '',
        title: '',
        content: '',
        files: '',
      },

      // 업데이트폼
      fileInputSize: 0, // 인풋파일사이즈
      saveFiles: [],  // 저장파일
      deleteFiles: [], // 삭제파일
      hideFile: [], // 숨김파일 (삭제시 임시보관)
    }
  },

  props: {
    board: Object, // 게시글
    files: Array, // 첨부파일
    condition: Object // 검색조건
  },

  created() {
    this.initBoardUpdate();
  },

  methods: {
    // 유효성검증
    validContent,
    validTitle,
    validPassword,
    validWriter,
    validCategory,

    /**
     * 업데이트를 처리합니다.
     */
    submitUpdate() {
      // if (!this.validForm()) {
      //   return false;
      // }

      const formData = this.createFormData();

      updateBoard(this.board.boardId, formData)
          .then(boardId => {
            this.$emit('updateBoard', boardId);
          })
          .catch(({data}) => {
            // 유효성검증에 실패한 필드의 에러메시지를 저장합니다.
            for (const field in data) {
              this.errorFields[field] = data[field];
            }
          });
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

      this.saveFiles.forEach(file => {
        formData.append('files', file)
      });
      this.deleteFiles.forEach(deleteFile => {
        formData.append('deleteFiles', deleteFile)
      })

      return formData;
    },

    /**
     * 폼데이터를 검증합니다.
     * @returns {boolean}
     */
    validForm() {
      this.errorFields.categoryId = validCategory(this.categoryId);
      this.errorFields.writer = validWriter(this.writer);
      this.errorFields.title = validTitle(this.title);
      this.errorFields.content = validContent(this.content);

      let formError = '';
      for (const field in this.errorFields) {
        formError += this.errorFields[field];
      }

      return formError.length === 0;
    },

    /**
     * 삭제할 파일을 임시배열에 추가합니다.
     * @param fileId
     * @param i
     */
    deleteFile(fileId, i) {
      this.deleteFiles.push(fileId);
      this.hideFile[i] = true;
      this.fileInputSize++;
    },

    /**
     * 초기데이터를 설정합니다.
     */
    initBoardUpdate() {
      this.categoryId = this.board.categoryId;
      this.writer = this.board.writer;
      this.title = this.board.title;
      this.content = this.board.content;
      this.fileInputSize = 3 - this.files.length;
    },
  },
}
</script>

<style scoped>

  .update-container {
    font-size: 14px;
  }

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
  }

  input {
    height: 30px;
    padding: 0 5px;
    width: 200px;
    margin: 5px 0;
  }

  .update-container > [class$='container'] {
    border-top: 1px solid black;
    display: flex;
  }


  .create-date, .update-date, .view-cnt, .writer-input-container,
  .password-input-container, .title-input-container {
    align-items: center;
    display: flex;
    flex-wrap: wrap;
    margin-left: 10px;
  }

  .writer-input-error, .password-input-error,
  .title-input-error, .content-text-error {
    color: red;
    flex-basis: 100%;
    font-size: 12px;
  }

  .category-select-container {
    align-items: center;
    display: flex;
    flex-wrap: wrap;
    margin: 5px 10px;
  }

  .category-select-error {
    flex-basis: 100%;
    font-size: 12px;
    color: red;
  }

  .title-input-container {
    width: 80%;
  }

  .title-input {
    width: 800px;
  }

  .content-text-container {
    width: 800px;;
    display: flex;
    flex-wrap: wrap;
    margin: 10px;
  }

  .content-text {
    width: 100%;
    resize: none;
    padding: 5px;
    height: 150px;
    margin-bottom: 5px;
  }

  .board-update-file-input-container {
    display: flex;
    flex-direction: column;
    margin: 5px 10px;
    align-items: flex-start;
  }

  .file-container {
    display: flex;
    gap: 10px;
  }

  .file-register-container{
  }

  .file-delete-btn {
    cursor: pointer;
    border: none;
    background-color: white;
  }


  .button-container {
    align-items: center;
    display: flex;
    justify-content: space-between;
    padding-top: 10px;
  }

  .button-container button {
    width: 70px;
    cursor: pointer;
  }

  .button-save {
    background: grey;
    border: 1px solid black;
    color: white;
  }

</style>