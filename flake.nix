{
  description = "Iconify Android development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    android-nixpkgs = {
      url = "github:tadfisher/android-nixpkgs";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, android-nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };

      android-sdk = android-nixpkgs.sdk.${system} (sdkPkgs: with sdkPkgs; [
        cmdline-tools-latest
        build-tools-35-0-0
        platform-tools
        platforms-android-35
      ]);
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = [
          android-sdk
          pkgs.jdk17
          pkgs.gradle
        ];

        ANDROID_HOME = "${android-sdk}/share/android-sdk";
        ANDROID_SDK_ROOT = "${android-sdk}/share/android-sdk";
        JAVA_HOME = "${pkgs.jdk17}";

        GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${android-sdk}/share/android-sdk/build-tools/35.0.0/aapt2";

        shellHook = ''
          echo "Iconify dev environment ready"
          echo "  Android SDK: $ANDROID_HOME"
          echo "  Java: $(java -version 2>&1 | head -1)"
        '';
      };
    };
}
