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
        <button class="btn btn-primary w-100" type="submit">로그인</button>
      </form>
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
</script>
