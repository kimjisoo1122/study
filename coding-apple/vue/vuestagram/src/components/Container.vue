<template>
  <div>
    <div v-if="step == 0">
      <Post v-for="(data, i) in postData" :key="i" :data="data"/>
    </div>
    <!-- 필터선택페이지 -->
    <div v-if="step == 1">
      <div class="upload-image" :class="filter" :style="`background-image: url(${imageUrl})`">
      </div>
      <div class="filters">
        <FilterBox @click="chgImg(filter)" v-for="(filter, i) in filters" :key="i" :filter="filter" :imageUrl="imageUrl">
          <template v-slot:a><span style="color: black">{{filter}}</span></template>
        </FilterBox>
      </div>
    </div>
    <!-- 글작성페이지 -->
    <div v-if="step == 2">
      <div class="upload-image" :class="filter" :style="`background-image: url(${imageUrl})`"></div>
      <div class="write">
        <textarea class="write-box" @input="$emit('content', $event.target.value)">write!</textarea>
      </div>
    </div>
  </div>
</template>

<script>
import Post from "@/components/Post.vue";
import FilterBox from "@/components/FilterBox.vue";

export default {
  name: "Container",
  components: {
    FilterBox,
    Post
  },
  data() {
    return {
      filters: [ "aden", "_1977", "brannan", "brooklyn", "clarendon", "earlybird", "gingham", "hudson",
        "inkwell", "kelvin", "lark", "lofi", "maven", "mayfair", "moon", "nashville", "perpetua",
        "reyes", "rise", "slumber", "stinson", "toaster", "valencia", "walden", "willow", "xpro2"],
      filter: ''
    }
  },
  props: {
    postData: Array,
    step: Number,
    imageUrl: String,

  },
  methods: {
    chgImg(filter) {
      console.log(filter);
      this.filter = filter;
      this.$emit('filter', filter);
    },
  }

}
</script>

<style scoped>
.upload-image {
  width: 100%;
  height: 450px;
  background: cornflowerblue;
  background-size: cover;
}

.filters {
  overflow-x: scroll;
  white-space: nowrap;
}



.filters::-webkit-scrollbar {
  height: 5px;
}

.filters::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.filters::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 5px;
}

.filters::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.write-box {
  border: none;
  width: 90%;
  height: 100px;
  padding: 15px;
  margin: auto;
  display: block;
  outline: none;
}
</style>