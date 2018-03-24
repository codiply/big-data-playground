import os

this_script = os.path.realpath(__file__)
logs_dir = os.path.dirname(this_script)

for root, dirs, files in os.walk(logs_dir):
    for f in files:
        filename = os.path.join(root, f)
        if not (filename == this_script or f.startswith('.')):
            os.remove(filename)