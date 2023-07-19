<!-- 게시글목록 헤더 컴포넌트입니다. -->
<template>

  <div class="board-list-header-container">

    <!-- 게시글갯수 -->
    <div class="board-list-cnt-container">
      <p class="board-list-cnt">{{ boardCnt }} 건</p>
    </div>

    <div class="board-header-container">
    <!-- 게시글목록 헤더 -->
      <ul class="board-header-list-container">
        <li class="board-header-category">카테고리</li>
        <li class="board-header-title">제목</li>
        <li class="board-header-writer">작성자</li>
        <li class="board-header-view">조회수</li>
        <li class="board-header-create">등록 일시</li>
        <li class="board-header-update">수정 일시</li>
      </ul>
    </div>

  </div>

</template>

<script>
export default {
  name: "BoardListHeader",
  props: {boardCnt: Number}
}
</script>

<style scoped>

  .board-list-cnt {
    font-size: 12px;
  }

  .board-header-list-container {
    align-items: center;
    border-bottom: 2px solid black;
    border-top: 1px solid #eee;
    display: flex;
    font-weight: bold;
    height: 30px;
    list-style: none;
    margin: 0;
    padding: 0;
    font-size: 14px;
    text-align: center;
  }

  .board-header-category {
    width: 15%;
  }

  .board-header-title {
    width: 45%;
  }

  .board-header-writer {
    width: 7%;
    margin-right: 15px;
  }

  .board-header-view {
    width: 5%;
  }

  .board-header-create {
    width: 15%;
    margin-left: 10px;
  }

  .board-header-update {
    width: 15%;
  }

</style>