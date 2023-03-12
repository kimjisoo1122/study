<template>
  <link rel="stylesheet" href="./css/App.css">
  <div class="header">
    <ul class="header-button-left">
      <li @click="step--">Cancel</li>
    </ul>
    <ul class="header-button-right">
      <li @click="step++" v-if="step == 1">Next</li>
      <li @click="publish" v-if="step == 2">발행</li>
    </ul>
    <img src="./assets/logo.png" class="logo"  alt=""/>
  </div>

  <Container :postData="postData" :step="step" :imageUrl="imageUrl" @content="content = $event" @filter="filter = $event"/>
  <button v-if="moreBtnVisible" @click="more" style="margin-bottom: 50px">더보기</button>

<!--  <div v-if="tab == 1">내용1</div>-->
<!--  <div v-else-if="tab == 2">내용2</div>-->
<!--  <div v-else-if="tab == 3">내용3</div>-->
<!--  <button @click="tab = i" v-for="(e, i) in 3" :key="++i">버튼{{i}}</button>-->

  <div class="footer">
    <ul class="footer-button-plus">
      <input @change="upload($event)" accept="image/*" type="file" id="file" class="inputfile" />
      <label for="file" class="input-plus">+</label>
    </ul>
  </div>
</template>

<script>
import Container from "@/components/Container.vue";
import postData from "@/data/postData";
import axios from "axios";

export default {
  name: 'App',
  components: {
    Container
  },
  data() {
    return {
      postData: postData,
      moreCnt: 0,
      moreBtnVisible: true,
      step: 0,
      imageUrl: '',
      content: '',
      filter: ''
    }
  },
  methods: {
    more() {
      if (this.moreCnt == 0) {
        axios.get('https://codingapple1.github.io/vue/more0.json')
            .then(({data}) => {
              this.postData.push(data);
              this.moreCnt++;
            })
      } else if (this.moreCnt == 1) {
        axios.get('https://codingapple1.github.io/vue/more1.json')
            .then(({data}) => {
              this.postData.push(data);
              this.moreBtnVisible = false;
            })
      }
    },
    upload(e) {
      let file = e.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
      this.step = 1;
    },
    publish() {
      const post = {
        name: "John Doe",
        userImage: "https://placeimg.com/200/200/people",
        postImage: this.imageUrl,
        likes: 20,
        date: Date.now(),
        liked: false,
        content: this.content,
        filter: this.filter
      };
      this.postData.unshift(post);
      this.step = 0;
    },
  }
};
</script>

<style>
@import './css/App.css';
</style>
