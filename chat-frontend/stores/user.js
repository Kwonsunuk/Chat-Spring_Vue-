// stores/user.js
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
  const username = ref('');
  const isAuthenticated = ref(false);

  function login(name) {
    username.value = name;
    isAuthenticated.value = true;
  }

  function logout() {
    username.value = '';
    isAuthenticated.value = false;
  }

  return {
    username,
    isAuthenticated,
    login,
    logout,
  };
});
