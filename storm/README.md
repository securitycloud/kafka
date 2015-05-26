Run Storm on cluster
============================

This following script run in folder <b>storm</b> (where .pom exist) one by one.

1.) Clean all PCs in cluster: kill all java programs and clean word directory.

        scripts/clean-cluster.sh

2.) Install all PCs in cluster: copy zookeeper, storm and configured them.

        scripts/install-cluster.sh

3.) Start all PCs in cluster: start zookeeper, nimbus, ui and supervisors.

        scripts/start-cluster.sh

4.) Deploy to cluster. Compile, deploy and run project.

        scripts/deploy-to-cluster.sh