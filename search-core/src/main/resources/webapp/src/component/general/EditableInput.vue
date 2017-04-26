<template>
    <div>
        <div v-if="!editing" @click="editing = true">
            <p>{{value}}</p>
        </div>
        <div v-if="editing" style="max-width:40px">
            <input type=":type" class="form-control" v-model="toValue" @keyup.enter="save" @blur="save">
        </div>
    </div>
</template>
<script>
export default {
    name:'editable-input',
    data() {
        return {
            toValue:this.value,
            editing : false
        }
    },
    props:['value','type'],
    methods: {
        save: function(){
            this.$data.editing = false
            this.$emit('save', this.$data.toValue)
        }
    }
}
</script>
<style>
</style>