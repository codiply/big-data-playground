FROM jupyter/all-spark-notebook

USER $NB_USER

ADD ./requirements.txt /tmp/requirements.txt
RUN pip install -r /tmp/requirements.txt

ENTRYPOINT start-notebook.sh --NotebookApp.token=''
