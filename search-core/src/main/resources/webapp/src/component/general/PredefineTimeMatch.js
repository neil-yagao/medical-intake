var qualifiedTime = ["早餐前", "早餐后", "午餐前", "午餐后", "晚餐前", "晚餐后", "临睡前"];

function install(Vue) {
    Vue.matchingPredefineTime = function(hour) {
        if (hour <= 9) {
            return ["早餐前", "早餐后"];
        } else if (hour <= 13) {
            return ["午餐前", "午餐后"];
        } else if (hour <= 19) {
            return ["晚餐前", "晚餐后"];
        } else {
            return ["临睡前"]
        }
    }

    Vue.qualifiedTime = function() {
        return qualifiedTime;
    }
}

export default install;