<!-- 게시글 첨부파일 컴포넌트 입니다. -->
<template>

  <!-- 첨부파일 다운로드링크  -->
  <div class="file-container">
    <span class="file-link" @click="fileDown(file.fileId)">{{ file.originalName }}</span>
  </div>

</template>

<script>


import {fileDown} from "@/api/fileService";

export default {
  name: "BoardFile",
  props: {
    file: Object // 첨부파일
  },

  methods: {
    fileDown,
  }
}
</script>

<style scoped>

  .file-container {
    padding: 3px 0;
    margin-bottom: 3px;
  }

  .file-link {
    color: blue;
    font-size: 14px;
    cursor: pointer;
  }

</style>