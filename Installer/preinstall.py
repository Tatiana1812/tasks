__author__ = 'Maks'

import os
import os.path
import zipfile
import shutil

from install_utils.download_utils import download_file

need_files_zip = "sch3dedit_files.zip"
create_installer_script = "create_x64_installer_script.iss"
create_installer_script_template = os.path.join("templates", "create_x64_installer_script_template.iss")

def get_current_version(work_dir):
    with open(os.path.join(work_dir, "..", "pom.xml")) as pom_file:
        lines = pom_file.readlines()
        for line in lines:
            start = line.find("<project.version>") + len("<project.version>")
            finish = line.find("</project.version>")
            if finish != -1:
                return line[start:finish]


def get_direct_link(url):
    new_url = url.replace("www.dropbox.com", "dl.dropboxusercontent.com")
    new_url = new_url.replace("?dl=0", "")
    return new_url

work_dir = os.path.abspath(os.path.dirname(__file__))
need_files_filename_full_path = os.path.join(work_dir, need_files_zip)
print "Work directory is {0}".format(work_dir)

if os.path.isfile(os.path.join(work_dir, need_files_zip)):
    pass
else:
    download_file(get_direct_link("https://www.dropbox.com/s/q4ou8e3djd7lrkw/sch3dedit_files.zip?dl=0"),
              need_files_filename_full_path)

with zipfile.ZipFile(need_files_filename_full_path, "r") as z:
    z.extractall(work_dir)

patch_directory = os.path.join(work_dir, "patch_bin")
if not os.path.exists(patch_directory):
    os.makedirs(patch_directory)

patch_exe = "patch.exe"
patch_exe_full_path = os.path.join(patch_directory, patch_exe)

if not os.path.exists(patch_exe_full_path):
    download_file(get_direct_link("https://www.dropbox.com/s/now4798mazsmqzx/patch.exe?dl=0"), patch_exe_full_path)

create_installer_script_x64 = create_installer_script
create_installer_script_x32 = create_installer_script_x64.replace("x64", "x32")
create_installer_script_x64_full_path = os.path.join(work_dir, create_installer_script_x64)
create_installer_script_x32_full_path = os.path.join(work_dir, create_installer_script_x32)
create_installer_script_template_full_path = os.path.join(work_dir, create_installer_script_template)

current_project_version = get_current_version(work_dir)
print "Current version of project is {0}".format(current_project_version)

with open(create_installer_script_template_full_path, "rt") as template_file, \
        open(create_installer_script_x64_full_path, "wt") as final_file:
    lines = template_file.readlines()
    for idx, line in enumerate(lines):
        if line.startswith('#define MyAppVersion "xxx"'):
            lines[idx] = '#define MyAppVersion "{0}"\n'.format( current_project_version )
    final_file.writelines(lines)

shutil.copy(create_installer_script_x64_full_path, create_installer_script_x32_full_path)
err = os.system("{0} {1} {2}".format(patch_exe_full_path, create_installer_script_x32_full_path,
                               os.path.join(work_dir, "to_x32.patch")))
if err != 0 :
    print "Error: Can't create x32 installer"

inno_installer = "inno_setup.exe"
inno_installer_full_path = os.path.join(work_dir, inno_installer)
if not os.path.exists(inno_installer_full_path):
    download_file("http://www.jrsoftware.org/download.php/is.exe",
                  inno_installer_full_path)








