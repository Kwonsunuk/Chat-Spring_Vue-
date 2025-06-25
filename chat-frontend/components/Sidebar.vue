<!-- components/Sidebar.vue -->
<template>
  <div class="p-3 border-end h-100">
    <div v-if="userStore.isAuthenticated">
      <!-- 로그인된 유저 정보 -->
      <p class="fw-bold">안녕하세요, {{ userStore.username }}님</p>
      <button @click="handleLogout" class="btn btn-sm btn-outline-secondary">로그아웃</button>
    </div>

    <div v-else>
      <!-- 로그인 폼 -->
      <form @submit.prevent="handleLogin">
        <div class="mb-2">
          <input v-model="username" class="form-control" placeholder="아이디" />
        </div>
        <div class="mb-2">
          <input v-model="password" type="password" class="form-control" placeholder="비밀번호" />
        </div>
        <button class="btn btn-primary w-100 mb-1" type="submit">로그인</button>
      </form>
      <button @click="showSignup = true" class="btn btn-primary w-100">회원가입</button>
    </div>
    <!-- 회원가입 모달 -->
    <div
      v-if="showSignup"
      class="modal-backdrop d-block"
      style="
        background-color: rgba(0, 0, 0, 0.5);
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
      "
    >
      <div class="modal d-block" tabindex="-1" style="margin-top: 10vh">
        <div class="modal-dialog">
          <div class="modal-content">
            <form @submit.prevent="handleSignup">
              <div class="modal-header">
                <h5 class="modal-title">회원가입</h5>
                <button type="button" class="btn-close" @click="showSignup = false"></button>
              </div>
              <div class="modal-body">
                <input v-model="signupUsername" class="form-control mb-2" placeholder="아이디" />
                <input
                  v-model="signupPassword"
                  type="password"
                  class="form-control"
                  placeholder="비밀번호"
                />
              </div>
              <div class="modal-footer">
                <button type="submit" class="btn btn-primary">가입하기</button>
                <button type="button" class="btn btn-secondary" @click="showSignup = false">
                  취소
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <button class="btn btn-secondary w-100" @click="navigateTo('/signup')">회원가입</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

import { useUserStore } from '@/stores/user';
import { navigateTo } from '#app';

const userStore = useUserStore();
// 로그인 입력값 바인딩용 변수
const username = ref('');
const password = ref('');
// 회원가입 입력값 바인딩용 변수
const signupUsername = ref('');
const signupPassword = ref('');
// 회원가입 버튼 클릭 시 나오는 모달창
const showSignup = ref(false);

// 로그인 요청 함수
const handleLogin = async () => {
  try {
    // 로그인 API 호출
    await $fetch('http://localhost:8080/api/users/login', {
      method: 'POST',
      body: { username: username.value, password: password.value },
      credentials: 'include',
    });
    // 스토어에 로그인 상태 반영
    userStore.login(username.value);
  } catch (e) {
    alert('로그인 실패');
  }
};

const handleLogout = async () => {
  await $fetch('http://localhost:8080/api/users/logout', {
    method: 'POST',
    credentials: 'include', // 쿠키 포함
  });
  userStore.logout();
  // jwt 쿠키 제거
  document.cookie = 'token=; Max-Age=0; path=/';
  // 스로어에 사용자 상태 초기화
  userStore.logout();
};

const handleSignup = async () => {
  try {
    const res = await $fetch('http://localhost:8080/api/users/signup', {
      method: 'POST',
      body: {
        username: signupUsername.value,
        password: signupPassword.value,
      },
    });
    alert(res);
    showSignup.value = false;
    signupUsername.value = '';
    signupPassword.value = '';
  } catch (err) {
    alert('회원가입 실패');
  }
};
</script>

<!-- 
  API 요청을 동기(synchronous) 방식으로 처리하지 않는 이유

  HTTP 요청은 기본적으로 시간이 걸리는 작업이다.
  브라우저 → 서버로 요청 전송 → 서버에서 처리 → 응답 수신
  이 모든 과정은 수 밀리초~수 초까지 소요될 수 있다.

  만약 이 과정을 **동기 방식**으로 처리한다면,
  응답이 돌아올 때까지 브라우저는 다음 코드를 실행하지 않고 기다리게 되며,
  이 동안 UI는 멈추고, 사용자 입력도 받지 못하는 상태가 된다.

  반면 비동기 방식을 사용하면,
  요청을 보낸 후에도 다음 코드가 계속 실행되고, 
  응답은 이후에 도착하는 시점에 처리된다.

  현재 코드에서는 `async/await`을 통해 비동기 흐름을 구현하고 있으며,
  이는 결과가 도착할 때까지 기다리되, 
  UI는 멈추지 않고 자연스럽게 작동할 수 있도록 도와준다.

  → 즉, 비동기 방식은 사용자 경험(UX)을 해치지 않으면서도
     서버와의 통신을 효율적으로 처리할 수 있게 해준다.
-->
