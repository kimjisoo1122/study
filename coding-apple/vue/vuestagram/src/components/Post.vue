<template>
  <link rel="stylesheet" href="../css/Post.css">
  <div class="post">
    <div class="post-header">
      <div class="profile" :style="`background-image: url(${data.userImage})`"></div>
      <span class="profile-name">{{ data.name }}</span>
    </div>
    <div class="post-body" :class="data.filter" :style="`background-image : url(${data.postImage})`"></div>
    <div class="post-content">
      <p>{{ data.like }}</p>
      <p><strong>{{ data.name }}</strong> {{ data.content }}</p>
      <p class="date">{{ data.date }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Post",
  data() {
  },
  props: {
    data: Object
  }
}
</script>

<style scoped>
@import "../css/Post.css";
</style>