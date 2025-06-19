<!-- pages/signup.vue -->
<template>
  <div class="container mt-5">
    <h2 class="mb-4">회원가입</h2>
    <form @submit.prevent="signup">
      <div class="mb-3">
        <input v-model="username" class="form-control" placeholder="아이디" />
      </div>
      <div class="mb-3">
        <input v-model="password" type="password" class="form-control" placeholder="비밀번호" />
      </div>
      <button type="submit" class="btn btn-primary w-100">가입하기</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';

import { navigateTo } from '#app';

const username = ref('');
const password = ref('');

const signup = async () => {
  try {
    const result = await $fetch('http://localhost:8080/api/users/signup', {
      method: 'POST',
      body: {
        username: username.value,
        password: password.value,
      },
    });
    alert(result);
    navigateTo('/');
  } catch (err) {
    alert(`회원가입 실패: ${err?.data || err.message}`);
  }
};
</script>
