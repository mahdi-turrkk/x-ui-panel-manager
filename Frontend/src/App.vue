<template>
  <div :class="{'dark-theme' : isDark , 'light-theme' : !isDark}"
       :style="{'direction' : useLocalization().getDirection}"
       class="appStaticColors bg-background-1 min-h-screen w-screen"
  >
    <router-view/>
  </div>
</template>

<script setup>
import {computed} from "vue";
import {useDataStore} from "./store/dataStore.js";
import {useLocalization} from "./store/localizationStore.js";

let isDark = computed(() => {
  return useDataStore().getDarkStatus
})
</script>

<style>
.appStaticColors {
  --color-error: 358 100% 50%;
  --color-success: 130 100% 35%;
  --color-primary-1: 216 100% 50%;
  --color-primary-2: 216 100% 40%;
  --color-primary-3: 216 100% 30%;
  --color-secondary-1: 47 100% 70%;
  --color-secondary-2: 47 100% 60%;
  --color-secondary-3: 47 100% 50%;
}

.light-theme {
  --color-background-1: 216 100% 98%;
  --color-background-2: 216 100% 96%;
  --color-background-3: 216 100% 94%;
  --color-info-1: 216 100% 85%;
  --color-info-2: 216 100% 75%;
  --color-info-3: 216 100% 5%;
  --color-warning: 50 100% 45%;
}

.dark-theme {
  --color-background-1: 216 100% 10%;
  --color-background-2: 216 100% 8%;
  --color-background-3: 216 100% 5%;
  --color-info-1: 216 100% 75%;
  --color-info-2: 216 100% 85%;
  --color-info-3: 216 100% 98%;
  --color-warning: 63 100% 50%;
}

.dir-rtl {
  direction: rtl;
}
</style>