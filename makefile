
uberjar:
	lein uberjar

deploy: uberjar
	scp -C ./target/uberjar/mbti-diary-0.1.0-SNAPSHOT-standalone.jar  mbti-diary.fun:/home/deploy/mbti-diary.fun/mbti-diary.jar 
	ssh mbti-diary.fun -t "systemctl restart mbti-diary.fun"
