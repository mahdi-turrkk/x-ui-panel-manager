<template>
  <div class="h-screen w-screen flex justify-center items-center bg-background-1">
    <div class="bg-background-3 px-6 py-6 rounded-xl flex justify-center items-center flex-col drop-shadow-xl">
      <div class="text-primary-1 text-3xl mb-10">
        <img :src="logoSrc" class="h-16 w-16">
      </div>
      <input type="text" :placeholder="local.username"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex relative">
        <input :type="showPass ? 'text' : 'password'" :placeholder="local.password"
               class="w-72 md:w-96 shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
               :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}"
        />
        <eye-icon class="h-5 w-5 text-info-2 absolute top-3 cursor-pointer"
                  :class="{'left-2' : isRtl , 'right-2' : !isRtl}" v-if="!showPass"
                  @click="showPass = !showPass"/>
        <eye-slash-icon class="h-5 w-5 text-info-2 absolute top-3 cursor-pointer"
                        :class="{'left-2' : isRtl , 'right-2' : !isRtl}" v-else
                        @click="showPass = !showPass"/>
      </div>
      <button class="rounded-xl bg-primary-3 w-full py-2 text-white hover:brightness-125 transition-all duration-150">
        {{ local.signIn }}
      </button>
      <div class="relative mt-4">
        <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
             @click="showLangMenu = !showLangMenu">{{ useLocalization().getFlag }}
          {{ useLocalization().getLanguage.toUpperCase() }}
        </div>
        <div class="absolute left-0 rounded-xl flex flex-col w-max bg-primary-1 bg-opacity-20" v-if="showLangMenu">
          <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
               @click="changeLanguage(['🇮🇷','fa' , 'rtl'])">🇮🇷 FA
          </div>
          <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
               @click="changeLanguage(['🇺🇸','en' , 'ltr'])">🇺🇸 EN
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import {
  EyeIcon,
  EyeSlashIcon
} from "@heroicons/vue/24/outline/index.js";
import {computed, ref} from "vue";
import {useDataStore} from "../../store/dataStore.js";
import {useLocalization} from "../../store/localizationStore.js";

let showPass = ref(false)

let logoSrc = computed(() => {
  return useDataStore().getDarkStatus ? '../src/assets/logo-white.png' : '../src/assets/logo-black.png'
})

let showLangMenu = ref(false)
let changeLanguage = (payload) => {
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
}

let local = computed(() => {
  return useLocalization().getLocal
})

let isRtl = computed(() => {
  return useLocalization().getDirection == 'rtl'
})

</script>