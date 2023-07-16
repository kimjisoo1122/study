<template>
  <div class="discount">
    <h4>지금 결제하면 {{discountRate}}% 할인</h4>
  </div>
</template>

<script>
export default {
  name: "Discount",
  props: {
    discountRate : Number,
  }
}
</script>

<style scoped>
 .discount {
   padding: 10px;
   background-color: #eee;
   margin: 10px;
   border-radius: 5px;
 }

</style>