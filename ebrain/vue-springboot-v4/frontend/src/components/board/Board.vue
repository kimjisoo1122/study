<template>

  <div class="board-container">

    <div class="board-category">
      {{ board.categoryName }}
    </div>

    <div class="board-title">
      <span v-if="board.hasFile" class="board-title-file">
        File
      </span>
      <router-link class="board-title-link"
                    :to="{
                      path: `/board/${board.boardId}`,
                      query: {
                        page: condition.page,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        {{ board.title }}
      </router-link>
    </div>

    <div class="board-writer">
      {{ board.writer }}
    </div>

    <div class="board-view">
      {{ board.viewCnt }}
    </div>

    <div class="board-create">
      {{ board.createDate }}
    </div>

    <div class="board-update">
      {{ board.updateDate }}
    </div>

  </div>

</template>

<script>
export default {
  name: "Board",
  props: {
    board: Object,
    condition: Object,
  }
}
</script>

<style scoped>

  .board-container {
    align-items: center;
    display: flex;
    font-size: 14px;
    text-align: center;
    padding: 5px 0;
    border-bottom: 1px solid #eee;
  }

  .board-category {
    width: 15%;
  }

  .board-title {
    width: 45%;
    text-align: left;
  }

  .board-title-link {
    text-decoration: none;
    color: black;
  }

  .board-title-file {
    color: cornflowerblue;
    margin-right: 5px;
  }

  .board-writer {
    width: 7%;
    margin-right: 15px;
  }

  .board-view {
    width: 5%;
  }

  .board-create {
    width: 15%;
    margin-left: 10px;
  }

  .board-update {
    width: 15%;
  }

</style>