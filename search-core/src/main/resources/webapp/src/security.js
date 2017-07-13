function securityModule() {
    var identityUrlAccessMapping = {
        'prison': [/.*working\/detail.*/, /.*recording.*/],
        'police': [/.*/],
        'medical': [/.*working\/.*/, /.*recording.*/],
        'docter': [/.*/]
    }
    return {
        isAllowTransfer(toUrl) {
            if (toUrl == '/') {
                return true
            }
            var currentIdentity = window.localStorage.getItem('identity')
            var allowUrlExp = identityUrlAccessMapping[currentIdentity];
            if (!allowUrlExp) {
                return false;
            }
            var allow = false;
            for (var index in allowUrlExp) {
                var urlToMatch = allowUrlExp[index];
                if ((urlToMatch instanceof RegExp) && toUrl.match(urlToMatch)) {
                    allow = true;
                }
                if ((urlToMatch instanceof String) && urlToMatch.indexOf(toUrl) >= 0) {
                    allow = true;
                }
            }
            return allow;
        },
        currentIdentity() {
            return window.localStorage.getItem('identity')
        },
        setCurrentIdentity(identity) {
            window.localStorage.setItem('identity', identity)
        },
        hasConfirmIntake() {
            return window.localStorage.getItem('confirm-intake');
        }
    }

}
export default securityModule()