import os
import shutil

this_script = os.path.realpath(__file__)
top_dir = os.path.dirname(this_script)

for root, dirs, files in os.walk(top_dir):
    for f in files:
        filename = os.path.join(root, f)
        if not (filename == this_script or f.endswith('.gitignore')):
            os.remove(filename)

def directory_contains_files(dir):
	for _, _, files in os.walk(dir):
		if files:
			return True
	return False

for root, _, _ in os.walk(top_dir):
	if not directory_contains_files(root):
		shutil.rmtree(root)
