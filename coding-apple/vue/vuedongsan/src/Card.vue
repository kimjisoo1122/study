<template>
  <div style="width: 30vw; margin: 0 auto">
    <img :src="원룸.image" alt="" class="room-img">
    <h4 @click="$emit('openModal', 원룸.id)">{{ 원룸.title }}</h4>
    <p>{{ 원룸.price }} 만원</p>
  </div>
</template>

<script>
export default {
  name: "Card",
  props: {
    원룸: Object,
    모달창열렸니: Boolean,
    누른거: Number
  }
}
</script>

<style scoped>
.room-img {
  width: 100%;
  margin-top: 40px;
  display: block;
}
</style>