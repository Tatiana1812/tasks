; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppBit "x64"
#define MyAppName "Sch3dedit"
#define MyAppVersion "xxx"
#define MyAppPublisher "Delaunay lab (Yaroslavl State University)"
#define MyAppExeName "Sch3dEdit.exe"
#define JoglLibsPath "sch3dedit_files\jogl_libs\windows"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{BD9FE353-0737-4351-A615-34DFE170DD1A}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName=C:/{#MyAppName}-{#MyAppBit}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
OutputBaseFilename=Sch3dEdit-{#MyAppVersion}-{#MyAppBit}-installer
Compression=lzma
SolidCompression=yes
ChangesAssociations=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "russian"; MessagesFile: "compiler:Languages\Russian.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 0,6.1

[Files]
Source: "..\target\Sch3dEdit.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\target\Sch3dEdit-with-debug-console.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\icons\*"; DestDir: "{app}\icons"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "..\themes\*"; DestDir: "{app}\themes"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "{#JoglLibsPath}\{#MyAppBit}\gluegen-rt.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\joal.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\jocl.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\jogl_cg.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\jogl_desktop.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\jogl_mobile.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\nativewindow_awt.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\nativewindow_win32.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\newt.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#JoglLibsPath}\{#MyAppBit}\soft_oal.dll"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\splash.jpg"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\config"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Registry]
Root: HKCR; Subkey: ".sc2";                             ValueData: "{#MyAppName}";          Flags: uninsdeletevalue; ValueType: string;  ValueName: ""
Root: HKCR; Subkey: ".sc3";                             ValueData: "{#MyAppName}";          Flags: uninsdeletevalue; ValueType: string;  ValueName: ""
Root: HKCR; Subkey: "{#MyAppName}";                     ValueData: "Scene {#MyAppName}";  Flags: uninsdeletekey;   ValueType: string;  ValueName: ""
Root: HKCR; Subkey: "{#MyAppName}\DefaultIcon";         ValueData: "{app}\{#MyAppExeName},0";           ValueType: string;  ValueName: ""
Root: HKCR; Subkey: "{#MyAppName}\shell\open\command";  ValueData: """{app}\{#MyAppExeName}"" ""%1""";  ValueType: string;  ValueName: ""
