<template>
  <div class="container">
    <router-view></router-view>
  </div>

</template>

<script>
/**
 * 컴포넌트 라이프사이클
 * 1. 컴포넌트 인스턴스를 생성한다
 * 2. data(), props, methods등 컴포넌트가 가진 값들을 초기화한다.
 * 3. beforeCreate 라이프사이클 훅을 호출한다. 이때 완전한 초기화상태는 아니라 data나 props등에 접근이 불가하다.
 * 4. created 훅을 호출한다 이때 컴포넌트는 완전한 초기화가 되었으며 이때는 모든 데이터에 접근이 가능하다.
 * 5. 컴포넌트가 완전히 생성했으니 컴포넌트를 컴파일하여 가상 DOM을 생성한다.
 * 6. 이때 자식 컴포넌트한테 props가 전송되고 자식컴포넌트들도 가상 DOM을 생성한다.
 * 7. 부모,자식 컴포넌트들의 모든 초기화가 이뤄지면 실제 DOM에 마운트를한다
 * 8. 자식 -> 부모순으로 beforeMount훅이 호출되고 실제 DOM에 마운트(화면에 모두 그려짐)됐으면 mounted훅이 호출된다.
 * 9. 컴포넌트들의 data()또는 compute, props 의 값이 변경되면 재랜더링을 하는데 이떄 update 훅이 호출된다.
 * 10. 다른 컴포넌트로 라우팅되면 destroy훅이 호출된다.
 */
import axios from "axios";

export default {
  name: 'App',
  data() {
    return {
      boardList: '',

    }
  },
  components: {
  },
  methods: {
    home() {
      axios.get('/board').then(res => console.log(res.data)).catch(err => console.error(err));
    },
  },
}
</script>

<style>

  .container {
    border: 1px solid black;
    margin: 150px auto;
    padding: 20px;
    width: 1000px;
  }

</style>
