#Cheking builds for all
#echo "--- checking big files"
#find . -iname "*.mp3" -print
#find . -iname "*.mp4" -print

echo " ==========================================="
echo " building skipped and you need to build anyting as you need" 
echo " ==========================================="
# cd packages/corexxx; npm run build; cd -
# cd packages/reactcorexxx; npm run build; cd -
# cd packages/nativecorexxx; npm run build; cd -

#cd packages/node-fest; npm run build; cd -
#cd packages/SimplePubSub; npm run build; cd -
#cd packages/SimpleStore; npm run build; cd -

#cd packages/trading50_common; npm run build; cd -
#cd packages/trading50_app; npm run build; cd -
#cd packages/trading50_app; npm run build; cd -
echo " ==========================================="
echo " Lnting the code..." 
echo " ==========================================="
tsfmt -r packages/**/*.ts


# ./node_modules/.bin/tsc  
# tsc

echo " ==========================================="
echo " Checking ...." 
echo " ==========================================="
git pull origin main
git add --all
git status
git commit -m "automated chkin"
git push
