ID=`ls -l $1| awk '{print $9,$5}'`
for id in $ID
do
echo $id
done