#!/bin/bash

# WhereIs helper script to create dummy Rocket.Chat users

rocketchat_url=http://192.168.99.100:3000
admin_username=jan
admin_password=123jan
number_of_users=32

CheckRequirements() {
    for tool in "$@"
	do
	    # echo "$tool"
	    command -v $tool >/dev/null 2>&1 || { echo "I require $tool but it's not installed (please install it by \"apt install $tool\"). Aborting." >&2; exit 1; }
	done
}

login() {
	response=`curl -s $rocketchat_url/api/v1/login -d "username=$admin_username&password=$admin_password" | sed -n -e 's/.*{"userId":"\([^\"]\{17\}\)","authToken":"\([^\"]\{43\}\)".*/\1	\2/p'`
	userId=${response:0:17}
	authToken=${response:17:47}
	echo userId = $userId
	echo authToken = $authToken
	echo
}

createSingleUser() {
	echo Creating user$1
	curl -H "X-Auth-Token: $authToken" -H "X-User-Id: $userId" -H "Content-type:application/json" http://docker:3000/api/v1/users.create -d '{"name": "Demo User '$1'", "email": "rocket.chat.ncdc+user'$1'@gmail.com", "password": "123user'$1'", "username": "user'$1'", "customFields": { "twitter": "@userstwitter" } }'
	echo
}

createUsers() {
	for i in `seq 1 $number_of_users`;
		do
			createSingleUser $i
        	done
}

CheckRequirements curl sed

login
createUsers
