<template>
  <div class="grid grid-cols-12">
    <div class="col-span-12 relative z-20 md:hidden">
      <div class="px-4 py-4">
        <div class="md:hidden flex items-center">
          <button @click="isHamburgerOpen = !isHamburgerOpen">
            <div class="border-t-2 border-t-info-3 w-8 my-2 duration-300 transition-all"
                 :class="{'rotate-45 translate-y-3' : isHamburgerOpen}"/>
            <div class="border-t-2 w-8 my-2 duration-300 transition-all"
                 :class="{'border-t-transparent' : isHamburgerOpen , 'border-t-info-3' : !isHamburgerOpen}"/>
            <div class="border-t-2 border-t-info-3 w-8 my-2 duration-300 transition-all"
                 :class="{'-rotate-45 -translate-y-2' : isHamburgerOpen}"/>
          </button>
          <div class="relative mr-2">
            <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer" @click="showLangMenu = !showLangMenu">{{useLocalization().getFlag}} {{useLocalization().getLanguage.toUpperCase()}}</div>
            <div class="absolute left-0 rounded-xl flex flex-col w-max bg-primary-1 bg-opacity-20" v-if="showLangMenu">
              <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl" @click="changeLanguage(['🇮🇷','fa'])">🇮🇷 FA</div>
              <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl" @click="changeLanguage(['🇺🇸','en'])">🇺🇸 EN</div>
            </div>
          </div>
          <button @click="changeThemeStatus"
                  class="p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200 ">
            <sun-icon class="w-6 h-6 text-info-3" v-if="isDark"/>
            <moon-icon class="w-6 h-6 text-info-3" v-if="!isDark"/>
          </button>
          <img :src="logoSrc" class="w-10 h-10 mr-auto cursor-pointer" @click="router.push('/')">
        </div>
      </div>
    </div>
    <div
        class="pt-14 md:pt-0 px-4 col-span-12 md:col-span-3 lg:col-span-2 bg-background-3 min-h-screen absolute top-0 right-0 left-0 bottom-0 md:relative z-10 flex flex-col space-y-4"
        v-if="isHamburgerOpen || isBigScreen">
      <div class="hidden md:flex items-center justify-between mt-4">
        <img :src="logoSrc" class="w-10 h-10 cursor-pointer" @click="router.push('/')">
        <div class="flex space-x-2 items-center">
          <div class="relative">
            <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer" @click="showLangMenu = !showLangMenu">{{useLocalization().getFlag}} {{useLocalization().getLanguage.toUpperCase()}}</div>
            <div class="absolute left-0 rounded-xl flex flex-col w-max bg-primary-1 bg-opacity-20" v-if="showLangMenu">
              <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl" @click="changeLanguage(['🇮🇷','fa'])">🇮🇷 FA</div>
              <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl" @click="changeLanguage(['🇺🇸','en'])">🇺🇸 EN</div>
            </div>
          </div>
          <button @click="changeThemeStatus"
                  class="p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200">
            <sun-icon class="w-6 h-6 text-info-3" v-if="isDark"/>
            <moon-icon class="w-6 h-6 text-info-3" v-if="!isDark"/>
          </button>
        </div>
      </div>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 hover:mr-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': $route.fullPath == '/admin'}" @click="router.push('/admin')">
        <squares2-x2-icon class="w-6 h-6 ml-2"/>
        {{ local.dashboard }}
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 hover:mr-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': $route.fullPath == '/admin/servers'}"
          @click="router.push('/admin/servers')">
        <server-stack-icon class="w-6 h-6 ml-2"/>
        {{ local.servers }}
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 hover:mr-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': $route.fullPath == '/admin/customers'}"
          @click="router.push('/admin/customers')">
        <UserGroupIcon class="w-6 h-6 ml-2"/>
        {{ local.customers }}
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 hover:mr-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': $route.fullPath == '/admin/subscriptions'}"
          @click="router.push('/admin/subscriptions')">
        <clipboard-document-list-icon class="text-info-3 w-6 h-6 ml-2"/>
        {{ local.subscriptions }}
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 hover:mr-2 transition-all duration-200"
          @click="router.push('/')">
        <arrow-left-on-rectangle-icon class="text-info-3 w-6 h-6 ml-2"/>
        {{ local.signOut }}
      </button>
    </div>
    <div class="col-span-12 md:col-span-9 lg:col-span-10 relative z-0 pt-4 px-4">
      <slot/>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {displayHelper} from "../helpers/displayHelper.js";
import {
  SunIcon,
  MoonIcon,
  ServerStackIcon,
  Squares2X2Icon,
  UserGroupIcon,
  ClipboardDocumentListIcon,
  ArrowLeftOnRectangleIcon
} from "@heroicons/vue/24/outline/index.js";
import {useDataStore} from "../store/dataStore.js";
import router from "../router/index.js";
import {useLocalization} from "../store/localizationStore.js";

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

const isBigScreen = computed(() => {
  return displayHelper(windowWidth.value).mdAndUp
})
let isHamburgerOpen = ref(false)

let isDark = computed(() => {
  return useDataStore().getDarkStatus
})

const changeThemeStatus = () => {
  useDataStore().changeDarkStatus()
}

let logoSrc = computed(() => {
  return useDataStore().getDarkStatus ? '../src/assets/logo-white.png' : '../src/assets/logo-black.png'
})

let showLangMenu = ref(false)
let changeLanguage = (payload)=>{
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
}

let local = computed(()=>{
  return useLocalization().getLocal
})

</script>