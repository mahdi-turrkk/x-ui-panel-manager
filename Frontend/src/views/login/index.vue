<template>
  <div class="h-screen w-screen flex justify-center items-center bg-background-1">
    <div class="bg-background-3 px-6 pt-6 pb-8 rounded-xl flex justify-center items-center flex-col drop-shadow-xl">
      <div class="text-primary-1 text-3xl mb-10">
        <img :src="logoSrc" class="h-16 w-16">
      </div>
      <input type="text" placeholder="نام کاربری"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex relative">
        <input :type="showPass ? 'text' : 'password'" placeholder="رمز عبور"
               class="w-72 md:w-96 shadow-lg mb-4 rounded-xl pr-4 pl-8 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <eye-icon class="h-5 w-5 text-info-2 absolute left-2 top-3 cursor-pointer" v-if="!showPass" @click="showPass = !showPass"/>
        <eye-slash-icon class="h-5 w-5 text-info-2 absolute left-2 top-3 cursor-pointer" v-else @click="showPass = !showPass"/>
      </div>
      <button class="rounded-xl bg-primary-3 w-full py-2 text-white hover:brightness-125 transition-all duration-150">
        ورود
      </button>
    </div>
  </div>
</template>
<script setup>
import {
  EyeIcon,
    EyeSlashIcon
} from "@heroicons/vue/24/outline/index.js";
import {computed, ref} from "vue";
import {useDataStore} from "../../store/index.js";

let showPass = ref(false)

let logoSrc = computed(() => {
  return useDataStore().getDarkStatus ? '../src/assets/logo-white.png' : '../src/assets/logo-black.png'
})

</script>