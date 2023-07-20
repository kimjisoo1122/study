<!-- 게시글 첨부파일 컴포넌트 입니다. -->
<template>

  <!-- 첨부파일 다운로드링크  -->
  <div class="file-container">
    <a class="file-link" :href="`/file/${file.fileId}`" >{{ file.originalName }}</a>
  </div>

</template>

<script>

export default {
  name: "BoardFile",
  props: {
    file: Object // 첨부파일
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
    text-decoration: navajowhite;
  }

</style>